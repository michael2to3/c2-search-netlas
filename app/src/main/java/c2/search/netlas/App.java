package c2.search.netlas;

import c2.search.netlas.c2detect.C2Detect;
import c2.search.netlas.c2detect.C2DetectImpl;
import c2.search.netlas.cli.CLArgumentsManager;
import c2.search.netlas.cli.OutputHandler;
import c2.search.netlas.config.ConfigManager;
import c2.search.netlas.config.DefaultConfigManager;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Results;
import c2.search.netlas.scheme.ResultsPrinter;
import java.io.PrintStream;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class App {
  private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
  private static String configFilename = "config.properties";
  private static PrintStream outputHandler;
  private static ConfigManager configManager;
  private static CommandLine commandLine;
  private static CLArgumentsManager clArgManager;

  private App() {}

  public static void main(final String[] args) {
    try {
      initialize(args);
      runApp(args);
    } catch (ParseException e) {
      LOGGER.error("Invalid arguments", e);
    }
  }

  public static void initialize(final String[] args) throws ParseException {
    configManager = new DefaultConfigManager(configFilename);
    final CommandLineParser commandLineParser = new DefaultParser();
    commandLine = commandLineParser.parse(getOptions(), args);
    clArgManager = new CLArgumentsManager(commandLine, configManager);
    outputHandler = new OutputHandler();
  }

  public static void runApp(final String[] args) {
    if (clArgManager.isHelp() || clArgManager.isInvalid()) {
      printHelp();
    } else if (clArgManager.isChangeApiKey() && clArgManager.isChangeTarget()
        || clArgManager.isChangeTarget()) {
      startC2Detect(clArgManager, outputHandler);
    } else if (clArgManager.isChangeApiKey()) {
      changeApiKey(clArgManager);
    } else {
      outputHandler.println("No target domain specified");
      printHelp();
    }
  }

  public static void setOutputHandler(final PrintStream stream) {
    App.outputHandler = stream;
  }

  public static void setCLArgumentsManager(final CLArgumentsManager clArgManager) {
    App.clArgManager = clArgManager;
  }

  public static CLArgumentsManager getCLArgumentsManager() {
    return clArgManager;
  }

  public static String getConfigFileName() {
    return configFilename;
  }

  public static void setConfigFileName(final String configFilename) {
    App.configFilename = configFilename;
  }

  private static void printHelp() {
    final HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("c2detect", getOptions());
  }

  private static void changeApiKey(final CLArgumentsManager clArgManager) {
    final String apikey = clArgManager.getApiKey();
    clArgManager.setApiKey(apikey);
  }

  private static void startC2Detect(
      final CLArgumentsManager clArgManager, final PrintStream outputHandler) {
    final String apikey = clArgManager.getApiKey();
    final Host host = clArgManager.getTarget();
    NetlasCache.changeApiKey(apikey);
    final C2Detect c2Detect = new C2DetectImpl(host);

    final Results results = c2Detect.run();

    final ResultsPrinter printer = new ResultsPrinter(results);
    printer.print(outputHandler, clArgManager.isVerbose(), clArgManager.isJson());
  }

  private static Options getOptions() {
    final Options options = new Options();
    final Option setOption =
        Option.builder("s")
            .longOpt("set")
            .hasArg(true)
            .argName("API_KEY")
            .desc("Set the API key to use for the application")
            .build();
    final Option targetOption =
        Option.builder("t")
            .longOpt("target")
            .hasArg(true)
            .argName("TARGET_DOMAIN")
            .desc("Set the target domain for the application")
            .build();
    final Option portOption =
        Option.builder("p")
            .longOpt("port")
            .hasArg(true)
            .argName("TARGET_PORT")
            .desc("Set the target port for the application")
            .build();
    final Option jsonOption =
        Option.builder("j").longOpt("json").hasArg(false).desc("Print in JSON format").build();
    final Option printVerbosOption =
        Option.builder("v").longOpt("verbose").hasArg(false).desc("Print verbose output").build();
    final Option helpOption =
        Option.builder("h").longOpt("help").desc("Print this help message").build();

    final List<Option> list =
        List.of(setOption, targetOption, portOption, jsonOption, printVerbosOption, helpOption);
    for (final Option option : list) {
      options.addOption(option);
    }
    return options;
  }
}
