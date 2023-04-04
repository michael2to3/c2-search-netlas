package c2.search.netlas;

import c2.search.netlas.classscanner.Checker;
import c2.search.netlas.classscanner.FieldValues;
import c2.search.netlas.cli.CLArgumentsManager;
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
  private FieldValues fields;
  private NetlasWrapper netlas;
  private PrintStream stream;
  private Checker checker;
  private CLArgumentsManager cmd;

  public C2Detect(CLArgumentsManager cmd, PrintStream stream) {
    LOGGER.info("Initializing C2Detect");
    this.cmd = cmd;
    this.stream = stream;
  }

  public void setStream(PrintStream stream) {
    this.stream = stream;
  }

  public void setCommandLineArgumentsManager(CLArgumentsManager cmd) {
    this.cmd = cmd;
  }

  public void setFields(FieldValues fields) {
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
      throws ClassNotFoundException,
          IOException,
          ParseException,
          IllegalAccessException,
          InstantiationException,
          InvocationTargetException,
          NoSuchMethodException,
          SecurityException {
    printWelcomMessage();
    setup(args);
    runChecker();
  }

  private void printWelcomMessage() {
    stream.println("c2detect: start scanning for C2");
    stream.flush();
    stream.println("Host: " + cmd.getHost());
    stream.flush();
  }

  protected Checker createChecker(FieldValues fields) throws ClassNotFoundException, IOException {
    return new Checker(fields);
  }

  private void runChecker()
      throws IllegalAccessException,
          InstantiationException,
          InvocationTargetException,
          NoSuchMethodException,
          SecurityException {
    Results responses = checker.run();
    printResponses(responses, cmd.isVerbose());
  }

  private FieldValues createFields(Host host, NetlasWrapper netlas) {
    FieldValues fields = new FieldValues();
    fields.setField(Host.class, host);
    fields.setField(NetlasWrapper.class, netlas);
    fields.setField(Netlas.class, netlas.getNetlas());
    fields.setField(Socket.class, getSocket(host, cmd.getSocketTimeoutMs()));
    return fields;
  }

  private void printResponses(Results responses, boolean verbose) {
    responses.print(stream, verbose);
  }

  protected static Socket getSocket(Host host, int socketTimeout) {
    try {
      Socket socket = new Socket(host.getTarget(), host.getPort());
      socket.setSoTimeout(socketTimeout);
      return socket;
    } catch (IOException e) {
      return null;
    }
  }
}
