package c2.search.netlas.cli;

import c2.search.netlas.*;
import c2.search.netlas.scheme.Host;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseCmdArgs {
  private static final int DEFAULT_SOCKET_TIMEOUT_MS = 1000;
  private final Logger LOGGER = LoggerFactory.getLogger(C2Detect.class);
  private final Config config;
  private CommandLine cmd;

  public ParseCmdArgs(CommandLine cmd, Config config) {
    LOGGER.info("Parsing command line arguments");
    this.cmd = cmd;
    this.config = config;
  }

  public Host getHost() {
    LOGGER.info("Getting host from command line arguments");
    String domain = cmd.getOptionValue("t");
    int port = getTargetPort();
    return new Host(domain, port);
  }

  public int getTargetPort() {
    LOGGER.info("Getting target port from command line arguments");
    String portStr = cmd.getOptionValue("p");

    try {
      return Integer.parseInt(portStr);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid target port: " + portStr);
    }
  }

  public boolean isHelp() {
    LOGGER.info("Checking if help was requested");
    return cmd.hasOption("h");
  }

  public boolean isVerbose() {
    LOGGER.info("Checking if verbose was requested");
    return cmd.hasOption("v");
  }

  public void setApiKey(String apiKey) {
    LOGGER.info("Setting API key");
    config.save("api.key", apiKey);
  }

  public String getApiKey() {
    LOGGER.info("Getting API key");
    return config.get("api.key");
  }

  public int getSocketTimeoutMs() {
    LOGGER.info("Getting socket timeout");
    return getSocketTimeoutMs(DEFAULT_SOCKET_TIMEOUT_MS);
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
}
