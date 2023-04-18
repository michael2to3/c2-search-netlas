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

  public CLArgumentsManager(final CommandLine cmd, final ConfigManager config) {
    this.cmd = cmd;
    this.config = config;
  }

  public boolean isInvalid() {
    return cmd == null;
  }

  public Host getTarget() {
    final String domain = cmd.getOptionValue("t");
    final int port = getTargetPort();
    return new Host(domain, port);
  }

  private int getTargetPort() {
    final String portStr = cmd.getOptionValue("p");

    int port = DEFAULT_PORT;
    try {
      port = Integer.parseInt(portStr);
    } catch (NumberFormatException e) {
      LOGGER.warn("Invalid port number: {}", portStr);
    }
    return port;
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

  public boolean isChangeTarget() {
    return cmd.hasOption("t");
  }

  public boolean isChangePort() {
    return cmd.hasOption("p");
  }

  public void setApiKey(final String apiKey) {
    config.save(PATH_API_KEY, apiKey);
  }

  public String getApiKey() {
    String apiKey = cmd.getOptionValue("s");
    if (!(cmd.hasOption("t") && cmd.hasOption("s")) && !cmd.hasOption("s")) {
      apiKey = config.get(PATH_API_KEY);
    }
    return apiKey;
  }
}
