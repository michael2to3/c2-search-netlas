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
import netlas.java.Netlas;
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
  private static String CONFIG_FILENAME = "config.properties";
  private static PrintStream outputHandler = new OutputHandler();

  private App() {}

  public static void main(String[] args) {
    try {
      runApp(args);
    } catch (ParseException e) {
      LOGGER.error("Invalid arguments", e);
    }
  }

  private static void runApp(String[] args) throws ParseException {
    ConfigManager configManager = new DefaultConfigManager(CONFIG_FILENAME);
    CommandLineParser commandLineParser = new DefaultParser();
    CommandLine commandLine = commandLineParser.parse(getOptions(), args);

    CLArgumentsManager clArgumentsManager = new CLArgumentsManager(commandLine, configManager);

    if (clArgumentsManager.isHelp() || clArgumentsManager.isInvalid()) {
      printHelp();
    } else if (clArgumentsManager.getHost() == null) {
      LOGGER.error("No target domain specified");
    } else if (clArgumentsManager.isChangeApiKey() && clArgumentsManager.getHost() != null) {
      startC2Detect(clArgumentsManager, outputHandler);
    } else if (clArgumentsManager.getHost() != null) {
      startC2Detect(clArgumentsManager, outputHandler);
    } else if (clArgumentsManager.isChangeApiKey()) {
      changeApiKey(clArgumentsManager);
    } else {
      outputHandler.println("No target domain specified");
      printHelp();
    }
  }

  public static void setOutputHandler(final PrintStream stream) {
    App.outputHandler = stream;
  }

  private static void printHelp() {
    final HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("c2detect", getOptions());
  }

  private static void changeApiKey(final CLArgumentsManager clArgumentsManager) {
    final String apikey = clArgumentsManager.getApiKey();
    clArgumentsManager.setApiKey(apikey);
  }

  private static void startC2Detect(
      final CLArgumentsManager clArgumentsManager, final PrintStream outputHandler) {
    final String apikey = clArgumentsManager.getApiKey();
    Host host = clArgumentsManager.getHost();
    Netlas netlas = Netlas.newBuilder().setApiKey(apikey).build();
    C2Detect c2Detect = new C2DetectImpl(host, netlas);

    Results results = c2Detect.run();

    final ResultsPrinter printer = new ResultsPrinter(results);
    printer.print(outputHandler, clArgumentsManager.isVerbose());
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
    final Option printVerbosOption =
        Option.builder("v").longOpt("verbose").hasArg(false).desc("Print verbose output").build();
    final Option helpOption =
        Option.builder("h").longOpt("help").desc("Print this help message").build();

    final List<Option> list =
        List.of(setOption, targetOption, portOption, printVerbosOption, helpOption);
    for (final Option option : list) {
      options.addOption(option);
    }
    return options;
  }
}
