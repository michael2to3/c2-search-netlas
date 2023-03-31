package c2.search.netlas;

import c2.search.netlas.classscanner.AnnotatedFieldValues;
import c2.search.netlas.classscanner.Checker;
import c2.search.netlas.cli.Config;
import c2.search.netlas.cli.ParseCmdArgs;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Results;
import c2.search.netlas.target.NetlasWrapper;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import netlas.java.Netlas;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class C2Detect {
  private static final int DEFAULT_SOCKET_TIMEOUT_MS = 1000;
  private final Logger LOGGER = LoggerFactory.getLogger(C2Detect.class);
  private final Options options;
  private AnnotatedFieldValues fields;
  private Host host;
  private NetlasWrapper netlas;
  private PrintStream stream;
  private Checker checker;
  private ParseCmdArgs cmd;

  public C2Detect(ParseCmdArgs cmd, Options options, PrintStream stream) {
    LOGGER.info("Initializing C2Detect");
    this.cmd = cmd;
    this.options = options;
    this.stream = stream;
  }

  public void setFields(AnnotatedFieldValues fields) {
    this.fields = fields;
  }

  public void setup(String[] args) throws IOException, ParseException, ClassNotFoundException {
    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = parser.parse(options, args);

    parseCommandLineArgs(cmd);
    if (host != null) {
      this.netlas = new NetlasWrapper(apiKey, host);
      this.fields = createFields(host, netlas);
      this.checker = createChecker(fields);
    }
  }

  public void run(String[] args)
      throws ClassNotFoundException, IOException, ParseException, IllegalAccessException,
          InstantiationException, InvocationTargetException, NoSuchMethodException,
          SecurityException {
    setup(args);
    run();
  }

  public void run()
      throws IllegalAccessException, InstantiationException, InvocationTargetException,
          NoSuchMethodException, SecurityException {
    LOGGER.info("Running C2Detect");
    if (host != null) {
      runChecker();
    }
  }

  protected Checker createChecker(AnnotatedFieldValues fields)
      throws ClassNotFoundException, IOException {
    return new Checker(fields);
  }

  protected void printHelp() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("c2detect", options);
  }

  private void runChecker()
      throws IllegalAccessException, InstantiationException, InvocationTargetException,
          NoSuchMethodException, SecurityException {
    Results responses = checker.run();
    printResponses(responses, verbose);
  }

  private void parseCommandLineArgs(CommandLine cmd) {
    if (cmd.hasOption("h")) {
      printHelp();
      return;
    }

    if (cmd.hasOption("s")) {
      setApiKey(cmd.getOptionValue("s"));
      return;
    }

    this.verbose = cmd.hasOption("v");
    this.apiKey = getApiKey();
    this.host = createHost(cmd);
  }

  private void setApiKey(String apiKey) {
    config.save("api.key", apiKey);
  }

  private String getApiKey() {
    String apiKey = config.get("api.key");
    if (apiKey == null || apiKey.isEmpty()) {
      throw new IllegalArgumentException("API key is required");
    }
    return apiKey;
  }

  private AnnotatedFieldValues createFields(Host host, NetlasWrapper netlas) {
    AnnotatedFieldValues fields = new AnnotatedFieldValues();
    fields.setField(Host.class, host);
    fields.setField(NetlasWrapper.class, netlas);
    fields.setField(Netlas.class, netlas.getNetlas());
    fields.setField(Socket.class, getSocket(host, getSocketTimeoutMs(DEFAULT_SOCKET_TIMEOUT_MS)));
    return fields;
  }

  private void printResponses(Results responses, boolean verbose) {
    responses.print(stream, verbose);
  }

  private int getSocketTimeoutMs(int defaultTimeout) {
    String timeout = config.get("socket.timeout");
    if (timeout == null) {
      return defaultTimeout;
    }
    try {
      return Integer.parseInt(timeout);
    } catch (NumberFormatException e) {
      return defaultTimeout;
    }
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
