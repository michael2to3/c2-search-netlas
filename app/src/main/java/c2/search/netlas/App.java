package c2.search.netlas;

import c2.search.netlas.cli.CommandLineArgumentsManager;
import c2.search.netlas.cli.Config;
import java.io.PrintStream;
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
  private static Config config = new Config(CONFIG_FILENAME);
  private static PrintStream out = System.out;
  private static C2Detect c2Detect = null;

  public static Logger getLogger() {
    return LOGGER;
  }

  public static PrintStream getOut() {
    return out;
  }

  public static void setOut(PrintStream out) {
    App.out = out;
  }

  public static String getConfigFilename() {
    return CONFIG_FILENAME;
  }

  public static Config getConfig() {
    return config;
  }

  public static void setConfig(Config config) {
    App.config = config;
  }

  public static CommandLineArgumentsManager getParseCmdArgs(String[] args) {
    CommandLine cmd = null;
    CommandLineParser parser = getDefaultParser();
    try {
      cmd = parser.parse(setupOptions(), args);
    } catch (ParseException e) {
      LOGGER.error("Error parsing command line arguments", e);
    }

    CommandLineArgumentsManager parseCmdArgs = new CommandLineArgumentsManager(cmd, config);
    return parseCmdArgs;
  }

  public static C2Detect getC2Detect() {
    return c2Detect;
  }

  public static void setC2Detect(C2Detect c2Detect) {
    App.c2Detect = c2Detect;
  }

  private static C2Detect getC2Detect(CommandLineArgumentsManager args) {
    return new C2Detect(args, out);
  }

  public static void main(String[] args) {
    CommandLineArgumentsManager parseCmdArgs = getParseCmdArgs(args);
    c2Detect = getC2Detect(parseCmdArgs);

    if (parseCmdArgs.isHelp()) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("c2detect", setupOptions());
    } else if (parseCmdArgs.isChangeApiKey()) {
      parseCmdArgs.setApiKey(parseCmdArgs.getApiKey());
    } else {
      startScan(args);
    }
  }

  public static void startScan(String[] args) {
    try {
      c2Detect.run(args);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      System.exit(1);
    }
  }

  protected static CommandLineParser getDefaultParser() {
    return new DefaultParser();
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
