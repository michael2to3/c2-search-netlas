package c2.search.netlas.cli;

import c2.search.netlas.*;
import c2.search.netlas.scheme.Host;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CLArgumentsManager {
  private static final int DEFAULT_SOCKET_TIMEOUT_MS = 1000;
  private static final Logger LOGGER = LoggerFactory.getLogger(C2Detect.class);
  private static final String PATH_API_KEY = "api.key";
  private final Config config;
  private final CommandLine cmd;
  private final boolean isInvalid;

  public CLArgumentsManager(final CommandLine cmd, final Config config) {
    LOGGER.info("Parsing command line arguments");
    this.cmd = cmd;
    this.config = config;
    this.isInvalid = cmd == null;
  }

  public boolean isInvalid() {
    return isInvalid;
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

    try {
      return Integer.parseInt(portStr);
    } catch (final NumberFormatException e) {
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
    if (cmd.hasOption("s")) {
      return cmd.getOptionValue("s");
    }
    return config.get(PATH_API_KEY);
  }

  public int getSocketTimeoutMs() {
    LOGGER.info("Getting socket timeout");
    return getSocketTimeoutMs(DEFAULT_SOCKET_TIMEOUT_MS);
  }

  private int getSocketTimeoutMs(final int defaultTimeout) {
    final String timeout = config.get("socket.timeout");
    if (timeout == null) {
      return defaultTimeout;
    }
    try {
      return Integer.parseInt(timeout);
    } catch (final NumberFormatException e) {
      return defaultTimeout;
    }
  }
}
