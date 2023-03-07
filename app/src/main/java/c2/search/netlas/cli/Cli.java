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

import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.metasploit.Metasploit;

public class Cli {
  private static final Logger LOGGER = LoggerFactory.getLogger(Cli.class);
  private final Config config;

  public Cli() {
    this.config = new Config();
  }

  public void run(final PrintStream stream, String[] args) {
    Options optionsWithConfig = OptionsCmd.get();
    CommandLineParser parser = new DefaultParser();

    try {
      CommandLine cmd = parser.parse(optionsWithConfig, args);
      if(cmd.hasOption("debug")) {

      }
      if (cmd.hasOption("s")) {
        LOGGER.debug("Command: {}", cmd.getOptionValue("s"));
        config.setApi(cmd.getOptionValue("s"));
      }
      if (cmd.hasOption("g")) {
        LOGGER.debug("Command: {}", cmd.getOptionValue("g"));
        stream.println("Your API key is: " + config.getApi());
      }
      Host host = new Host();
      if (cmd.hasOption("t")) {
        LOGGER.debug("Command: {}", cmd.getOptionValue("t"));
        host.setHost(cmd.getOptionValue("t"));
      }
      if(cmd.hasOption("p")) {
        LOGGER.debug("Command: {}", cmd.getOptionValue("p"));
        host.setPort(Integer.parseInt(cmd.getOptionValue("p")));
      }

      var m = new Metasploit().run(host);
      System.out.println(m.toString());

    } catch (ParseException e) {
      System.out.println("Error: " + e.getMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("c2detect", optionsWithConfig);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
