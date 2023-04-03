package c2.search.netlas;

import c2.search.netlas.classscanner.AnnotatedFieldValues;
import c2.search.netlas.classscanner.Checker;
import c2.search.netlas.cli.ParseCmdArgs;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Results;
import c2.search.netlas.target.NetlasWrapper;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import netlas.java.Netlas;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class C2Detect {
  private final Logger LOGGER = LoggerFactory.getLogger(C2Detect.class);
  private AnnotatedFieldValues fields;
  private NetlasWrapper netlas;
  private PrintStream stream;
  private Checker checker;
  private ParseCmdArgs cmd;

  public C2Detect(ParseCmdArgs cmd, PrintStream stream) {
    LOGGER.info("Initializing C2Detect");
    this.cmd = cmd;
    this.stream = stream;
  }

  public void setFields(AnnotatedFieldValues fields) {
    this.fields = fields;
  }

  public void setup(String[] args) throws IOException, ParseException, ClassNotFoundException {
    if (cmd.getHost() != null) {
      this.netlas = new NetlasWrapper(cmd.getApiKey(), cmd.getHost());
      this.fields = createFields(cmd.getHost(), netlas);
      this.checker = createChecker(fields);
    }
  }

  public void run(String[] args)
      throws ClassNotFoundException, IOException, ParseException, IllegalAccessException,
          InstantiationException, InvocationTargetException, NoSuchMethodException,
          SecurityException {
    printWelcomMessage();
    setup(args);
    run();
  }

  public void run()
      throws IllegalAccessException, InstantiationException, InvocationTargetException,
          NoSuchMethodException, SecurityException {
    LOGGER.info("Running C2Detect");
    if (cmd.getHost() != null) {
      runChecker();
    }
  }

  private void printWelcomMessage() {
    stream.println("c2detect: start scanning for C2");
    stream.flush();
    stream.println("Host: " + cmd.getHost());
    stream.flush();
  }

  protected Checker createChecker(AnnotatedFieldValues fields)
      throws ClassNotFoundException, IOException {
    return new Checker(fields);
  }

  private void runChecker()
      throws IllegalAccessException, InstantiationException, InvocationTargetException,
          NoSuchMethodException, SecurityException {
    Results responses = checker.run();
    printResponses(responses, cmd.isVerbose());
  }

  private AnnotatedFieldValues createFields(Host host, NetlasWrapper netlas) {
    AnnotatedFieldValues fields = new AnnotatedFieldValues();
    fields.setField(Host.class, host);
    fields.setField(NetlasWrapper.class, netlas);
    fields.setField(Netlas.class, netlas.getNetlas());
    fields.setField(Socket.class, getSocket(host, cmd.getSocketTimeoutMs()));
    return fields;
  }

  private void printResponses(Results responses, boolean verbose) {
    responses.print(stream, verbose);
  }

  private Socket getSocket(Host host, int socketTimeout) {
    try {
      Socket socket = new Socket(host.getTarget(), host.getPort());
      socket.setSoTimeout(socketTimeout);
      return socket;
    } catch (IOException e) {
      return null;
    }
  }
}
