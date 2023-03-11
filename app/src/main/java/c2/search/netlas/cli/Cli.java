package c2.search.netlas.cli;

import c2.search.netlas.scheme.Host;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cli {
  private static final Logger LOGGER = LoggerFactory.getLogger(Cli.class);
  private final Config config;

  public Cli() {
    this("config.properties");
  }

  public Cli(String configFile) {
    this.config = new Config(configFile);
  }

  public Host run(final PrintStream stream, String[] args)
      throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
          NoSuchMethodException, InvocationTargetException {
    Options optionsWithConfig = OptionsCmd.get();
    CommandLineParser parser = new DefaultParser();

    CommandLine cmd = null;
    try {
      cmd = parser.parse(optionsWithConfig, args);
    } catch (ParseException e) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("c2detect", optionsWithConfig);
      return null;
    }
    if (cmd.hasOption("debug")) {
      // TODO: add debug option
    }

    if (cmd.hasOption("s")) {
      LOGGER.debug("Command: {}", cmd.getOptionValue("s"));
      String[] values = cmd.getOptionValues("s");
      for (String value : values) {
        String[] parts = value.split("=");
        if (parts.length == 2 && parts[0].matches("^\\w+(\\.\\w+)*$")) {
          config.save(parts[0], parts[1]);
        }
      }
      return null;
    }
    if (cmd.hasOption("g")) {
      LOGGER.debug("Command: {}", cmd.getOptionValue("g"));
      String get = cmd.getOptionValue("g");
      stream.println("From config: " + config.get(get));
      return null;
    }

    Host host = new Host();
    if (cmd.hasOption("t")) {
      LOGGER.debug("Command: {}", cmd.getOptionValue("t"));
      host.setTarget(cmd.getOptionValue("t"));
    }
    if (cmd.hasOption("p")) {
      LOGGER.debug("Command: {}", cmd.getOptionValue("p"));
      host.setPort(Integer.parseInt(cmd.getOptionValue("p")));
    }

    return host;
  }
}
