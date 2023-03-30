package c2.search.netlas;

import c2.search.netlas.classscanner.AnnotatedFieldValues;
import c2.search.netlas.classscanner.Checker;
import c2.search.netlas.cli.Config;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Results;
import c2.search.netlas.target.NetlasWrapper;
import java.io.IOException;
import java.net.Socket;
import netlas.java.Netlas;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class C2Detect {
  private final Logger LOGGER = LoggerFactory.getLogger(C2Detect.class);
  private final Config config;
  private final Options options;
  private static final int DEFAULT_SOCKET_TIMEOUT = 1000;

  public C2Detect(Config config, Options options) {
    LOGGER.info("Initializing C2Detect with config: {}, options: {}", config, options);
    this.config = config;
    this.options = options;
  }

  public void run(String[] args) throws Exception {
    LOGGER.info("Running C2Detect");

    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = parser.parse(options, args);

    if (cmd.hasOption("h")) {
      printHelp();
      return;
    }

    if (cmd.hasOption("s")) {
      config.save("api.key", cmd.getOptionValue("s"));
      return;
    }

    boolean verbose = cmd.hasOption("v");

    String apiKey = config.get("api.key");
    if (apiKey == null || apiKey.isEmpty()) {
      throw new IllegalArgumentException("API key is required");
    }

    Host host = createHost(cmd);
    NetlasWrapper netlas = new NetlasWrapper(apiKey, host);

    AnnotatedFieldValues fields = new AnnotatedFieldValues();
    fields.setField(Host.class, host);
    fields.setField(NetlasWrapper.class, netlas);
    fields.setField(Netlas.class, netlas.getNetlas());
    fields.setField(Socket.class, getSocket(host, getSocketTimeoutMs(DEFAULT_SOCKET_TIMEOUT)));

    Results responses = new Checker(fields).run();
    responses.print(System.out, verbose);
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

  protected static Host createHost(CommandLine cmd) {
    String domain = cmd.getOptionValue("t");
    if (domain == null) {
      throw new IllegalArgumentException("Target domain is not set");
    }

    int port;
    try {
      String portStr = cmd.getOptionValue("p");
      port = Integer.parseInt(portStr);
    } catch (Exception e) {
      throw new IllegalArgumentException("Target port is not set");
    }

    return new Host(domain, port);
  }

  protected void printHelp() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("c2detect", options);
  }
}
