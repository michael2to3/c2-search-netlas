package c2.search.netlas;

import c2.search.netlas.cli.Config;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Results;
import c2.search.netlas.target.Checker;
import c2.search.netlas.target.NetlasWrapper;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class C2Detect {
  private final Logger logger = LoggerFactory.getLogger(C2Detect.class);
  private final Config config;
  private final Options options;

  public C2Detect(Config config, Options options) {
    this.config = config;
    this.options = options;
  }

  public void run(String[] args) {
    try {
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
        logger.error("API key is not set");
        return;
      }

      Host host = createHost(cmd);
      if (host == null) {
        printHelp();
        return;
      }

      NetlasWrapper netlas = new NetlasWrapper(apiKey, host);
      Results responses = new Checker(netlas, host).run();
      responses.print(System.out, verbose);

    } catch (Exception e) {
      logger.error(e.getMessage());
      printHelp();
      System.exit(1);
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
