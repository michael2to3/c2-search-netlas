package c2.search.netlas;

import c2.search.netlas.cli.Config;
import c2.search.netlas.cli.ParseCmdArgs;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
  private static final String CONFIG_FILENAME = "config.properties";

  public static void main(String[] args) {
    Config config = new Config(CONFIG_FILENAME);
    CommandLineParser parser = new DefaultParser();
    CommandLine cmd;
    try {
      cmd = parser.parse(setupOptions(), args);
    } catch (ParseException e) {
      LOGGER.error("Error parsing command line arguments", e);
      System.exit(1);
      return;
    }
    ParseCmdArgs parseCmdArgs = new ParseCmdArgs(cmd, config);
    if (parseCmdArgs.isHelp()) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("c2detect", setupOptions());
      System.exit(0);
      return;
    }
    C2Detect c2Detect = new C2Detect(parseCmdArgs, System.out);
    try {
      c2Detect.run(args);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      System.exit(1);
    }
  }

  protected static Options setupOptions() {
    Options options = new Options();
    Option setOption =
        Option.builder("s")
            .longOpt("set")
            .hasArg(true)
            .argName("API_KEY")
            .desc("Set the API key to use for the application")
            .build();
    Option getOption =
        Option.builder("g")
            .longOpt("get")
            .hasArg(true)
            .argName("CONFIG_KEY")
            .desc("Get the value of the specified configuration key")
            .build();
    Option targetOption =
        Option.builder("t")
            .longOpt("target")
            .hasArg(true)
            .argName("TARGET_DOMAIN")
            .desc("Set the target domain for the application")
            .build();
    Option portOption =
        Option.builder("p")
            .longOpt("port")
            .hasArg(true)
            .argName("TARGET_PORT")
            .desc("Set the target port for the application")
            .build();
    Option printVerbosOption =
        Option.builder("v").longOpt("verbose").hasArg(false).desc("Print verbose output").build();
    Option helpOption = Option.builder("h").longOpt("help").desc("Print this help message").build();

    options.addOption(setOption);
    options.addOption(getOption);
    options.addOption(targetOption);
    options.addOption(portOption);
    options.addOption(helpOption);
    options.addOption(printVerbosOption);

    return options;
  }
}
