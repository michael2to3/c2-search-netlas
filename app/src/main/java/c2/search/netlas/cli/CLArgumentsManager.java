package c2.search.netlas.cli;

import c2.search.netlas.c2detect.C2Detect;
import c2.search.netlas.config.ConfigManager;
import c2.search.netlas.scheme.Host;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CLArgumentsManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(C2Detect.class);
  private static final String PATH_API_KEY = "api.key";
  private static final int DEFAULT_PORT = 443;
  private final ConfigManager config;
  private final CommandLine cmd;
  private final boolean invalid;

  public CLArgumentsManager(final CommandLine cmd, final ConfigManager config) {
    this.cmd = cmd;
    this.config = config;
    this.invalid = cmd == null;
  }

  public boolean isInvalid() {
    return invalid;
  }

  public Host getHost() {
    final String domain = cmd.getOptionValue("t");
    final int port = getTargetPort();
    return new Host(domain, port);
  }

  private int getTargetPort() {
    final String portStr = cmd.getOptionValue("p");

    try {
      return Integer.parseInt(portStr);
    } catch (NumberFormatException e) {
      LOGGER.warn("Invalid port number: {}", portStr);
      return DEFAULT_PORT;
    }
  }

  public boolean isHelp() {
    return cmd.hasOption("h");
  }

  public boolean isVerbose() {
    return cmd.hasOption("v");
  }

  public boolean isChangeApiKey() {
    return cmd.hasOption("s");
  }

  public void setApiKey(final String apiKey) {
    config.save(PATH_API_KEY, apiKey);
  }

  public String getApiKey() {
    String apiKey = config.get(PATH_API_KEY);
    if (cmd.hasOption("s")) {
      apiKey = cmd.getOptionValue("s");
    }
    return apiKey;
  }
}
