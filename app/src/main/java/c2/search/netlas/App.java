/**
 * The App class is the entry point of the c2detect application. It provides methods for parsing
 * command line arguments, setting and getting the configuration properties, and starting the scan
 * process.
 */
package c2.search.netlas;

import c2.search.netlas.cli.CLArgumentsManager;
import c2.search.netlas.cli.Config;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
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

public class App {
  private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
  private static final String CONFIG_FILENAME = "config.properties";
  private static Config config;
  private static PrintStream out;
  private static C2Detect c2detect;

  static {
    config = new Config(CONFIG_FILENAME);
    out = System.out;
    c2detect = new C2Detect(null, out);
  }

  /**
   * Get the output print stream for the application.
   *
   * @return The output print stream.
   */
  public static PrintStream getOut() {
    return out;
  }

  /**
   * Set the output print stream for the application.
   *
   * @param out The output print stream.
   */
  public static void setOut(final PrintStream out) {
    App.out = out;
  }

  /**
   * Get the filename of the configuration file.
   *
   * @return The filename of the configuration file.
   */
  public static String getConfigFilename() {
    return CONFIG_FILENAME;
  }

  /**
   * Get the configuration properties.
   *
   * @return The configuration properties.
   */
  public static Config getConfig() {
    return config;
  }

  /**
   * Set the configuration properties.
   *
   * @param config The configuration properties.
   */
  public static void setConfig(final Config config) {
    App.config = config;
  }

  /**
   * Parse the command line arguments and return a CLArgumentsManager object.
   *
   * @param args The command line arguments.
   * @return A CLArgumentsManager object.
   */
  public static CLArgumentsManager getParseCmdArgs(final String[] args) {
    CommandLine cmd = null;
    final CommandLineParser parser = getDefaultParser();
    try {
      cmd = parser.parse(setupOptions(), args);
    } catch (final ParseException e) {
      LOGGER.info("Error parsing command line arguments", e);
    }

    final CLArgumentsManager parseCmdArgs = new CLArgumentsManager(cmd, config);
    return parseCmdArgs;
  }

  /**
   * Get the C2Detect object.
   *
   * @return The C2Detect object.
   */
  public static C2Detect getC2detect() {
    return c2detect;
  }

  /**
   * Set the C2Detect object.
   *
   * @param c2Detect The C2Detect object.
   */
  public static void setC2detect(final C2Detect c2Detect) {
    App.c2detect = c2Detect;
  }

  /**
   * The main method of the application.
   *
   * @param args The command line arguments.
   */
  public static void main(final String[] args) {
    final CLArgumentsManager parseCmdArgs = getParseCmdArgs(args);
    c2detect.setCommandLineArgumentsManager(parseCmdArgs);

    if (parseCmdArgs.isInvalid() || parseCmdArgs.isHelp()) {
      final HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("c2detect", setupOptions());
    } else if (parseCmdArgs.isChangeApiKey()) {
      parseCmdArgs.setApiKey(parseCmdArgs.getApiKey());
    } else {
      startScan(args);
    }
  }

  /**
   * Start the scan process with the given command line arguments.
   *
   * @param args The command line arguments.
   */
  public static void startScan(final String[] args) {
    try {
      c2detect.run(args);
    } catch (ClassNotFoundException
        | IllegalAccessException
        | InstantiationException
        | InvocationTargetException
        | NoSuchMethodException
        | SecurityException
        | IOException
        | ParseException e) {
      LOGGER.error("Error running c2detect", e);
    }
  }

  /**
   * Get the default command line parser.
   *
   * @return The default command line parser.
   */
  protected static CommandLineParser getDefaultParser() {
    return new DefaultParser();
  }

  /**
   * Set up the command line options for the application.
   *
   * @return The options for the command line.
   */
  protected static Options setupOptions() {
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
