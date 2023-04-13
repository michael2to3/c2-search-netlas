package c2.search.netlas;

import c2.search.netlas.cli.CLArgumentsManager;
import c2.search.netlas.execute.Execute;
import c2.search.netlas.execute.FieldValues;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Results;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.PrintStream;
import netlas.java.Netlas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class C2Detect {
  private static final Logger LOGGER = LoggerFactory.getLogger(C2Detect.class);
  private FieldValues fields;
  private NetlasWrapper netlas;
  private PrintStream stream;
  private Execute checker;
  private CLArgumentsManager cmd;

  public C2Detect(final CLArgumentsManager cmd, final PrintStream stream) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Initializing C2Detect");
    }
    this.cmd = cmd;
    this.stream = stream;
  }

  public void setStream(final PrintStream stream) {
    this.stream = stream;
  }

  public void setCommandLineArgumentsManager(final CLArgumentsManager cmd) {
    this.cmd = cmd;
  }

  public void setFields(final FieldValues fields) {
    this.fields = fields;
  }

  public void setup(final String[] args) {
    if (cmd.getHost() != null) {
      this.netlas = getNetlasWrapper();
      this.fields = createFields(cmd.getHost(), netlas);
      this.checker = new Execute(fields);
    }
  }

  private NetlasWrapper getNetlasWrapper() {
    NetlasWrapper netlas = null;
    try {
      netlas = new NetlasWrapper(cmd.getApiKey(), cmd.getHost());
    } catch (JsonProcessingException e) {
      LOGGER.error("Failed to create NetlasWrapper", e);
    }
    return netlas;
  }

  public void run(final String[] args) {
    printWelcomMessage();
    setup(args);
    runChecker();
  }

  private void printWelcomMessage() {
    stream.println("c2detect: start scanning for C2");
    stream.flush();
    stream.println("Target: " + cmd.getHost());
    stream.flush();
  }

  private void runChecker() {
    final Results responses = checker.run();
    printResponses(responses, cmd.isVerbose());
  }

  private FieldValues createFields(final Host host, final NetlasWrapper netlas) {
    final FieldValues fields = new FieldValues();
    fields.setField(Host.class, host);
    fields.setField(NetlasWrapper.class, netlas);
    fields.setField(Netlas.class, netlas.getNetlas());
    return fields;
  }

  private void printResponses(final Results responses, final boolean verbose) {
    responses.print(stream, verbose);
  }
}
