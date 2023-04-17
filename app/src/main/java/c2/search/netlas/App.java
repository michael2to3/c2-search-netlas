package c2.search.netlas;

import c2.search.netlas.c2detect.C2Detect;
import c2.search.netlas.c2detect.C2DetectImpl;
import c2.search.netlas.cli.*;
import c2.search.netlas.cli.CLArgumentsManager;
import c2.search.netlas.config.ConfigManager;
import c2.search.netlas.config.DefaultConfigManager;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Results;
import c2.search.netlas.scheme.ResultsPrinter;
import java.util.List;
import netlas.java.Netlas;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public final class App {
  private static String CONFIG_FILENAME = "config.properties";

  public static void main(String[] args) {
    ConfigManager configManager = new DefaultConfigManager(CONFIG_FILENAME);
    CommandLineParser commandLineParser = new DefaultParser();
    CommandLine commandLine = null;
    OutputHandler outputHandler = new OutputHandler();
    try {
      commandLine = commandLineParser.parse(getOptions(), args);
    } catch (ParseException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }

    CLArgumentsManager clArgumentsManager = new CLArgumentsManager(commandLine, configManager);
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
