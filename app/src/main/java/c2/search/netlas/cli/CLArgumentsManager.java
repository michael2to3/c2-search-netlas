package c2.search.netlas.cli;

import c2.search.netlas.C2Detect;
import c2.search.netlas.scheme.Host;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CLArgumentsManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(C2Detect.class);
  private static final String PATH_API_KEY = "api.key";
  private final Config config;
  private final CommandLine cmd;
  private final boolean invalid;

  public CLArgumentsManager(final CommandLine cmd, final Config config) {
    LOGGER.info("Parsing command line arguments");
    this.cmd = cmd;
    this.config = config;
    this.invalid = cmd == null;
  }

  public boolean isInvalid() {
    return invalid;
  }

  public Host getHost() {
    LOGGER.info("Getting host from command line arguments");
    final String domain = cmd.getOptionValue("t");
    final int port = getTargetPort();
    return new Host(domain, port);
  }

  public int getTargetPort() {
    LOGGER.info("Getting target port from command line arguments");
    final String portStr = cmd.getOptionValue("p");

    int port = -1;
    try {
      port = Integer.parseInt(portStr);
    } catch (final NumberFormatException e) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Invalid port number: " + portStr);
      }
    }
    return port;
  }

  public boolean isHelp() {
    LOGGER.info("Checking if help was requested");
    return cmd.hasOption("h");
  }

  public boolean isVerbose() {
    LOGGER.info("Checking if verbose was requested");
    return cmd.hasOption("v");
  }

  public boolean isChangeApiKey() {
    LOGGER.info("Checking if change api key was requested");
    return cmd.hasOption("s");
  }

  public void setApiKey(final String apiKey) {
    LOGGER.info("Setting API key");
    config.save(PATH_API_KEY, apiKey);
  }

  public String getApiKey() {
    LOGGER.info("Getting API key");
    String apiKey = config.get(PATH_API_KEY);
    if (cmd.hasOption("s")) {
      apiKey = cmd.getOptionValue("s");
    }
    return apiKey;
  }
}
