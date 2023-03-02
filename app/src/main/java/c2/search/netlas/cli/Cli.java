package c2.search.netlas.cli;

import java.io.PrintStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cli {
  private static final Logger logger = LoggerFactory.getLogger(Cli.class);
  private final Config config;

  public Cli() {
    this.config = new Config(); 
  }

  public void run(final PrintStream stream, String[] args) {
    Options optionsWithConfig = OptionsCmd.get();
    CommandLineParser parser = new DefaultParser();

    try {
      CommandLine cmd = parser.parse(optionsWithConfig, args);
      if (cmd.hasOption("s")) {
        logger.debug("Command: {}", cmd.getOptionValue("s"));
        config.setApi(cmd.getOptionValue("s"));
      }
      if (cmd.hasOption("g")) {
        logger.debug("Command: {}", cmd.getOptionValue("g"));
        stream.println("Your API key is: " + config.getApi());
      }
      if (cmd.hasOption("t")) {
        logger.debug("Command: {}", cmd.getOptionValue("t"));
      }
    } catch (ParseException e) {
      System.out.println("Error: " + e.getMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("myapp", optionsWithConfig);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
