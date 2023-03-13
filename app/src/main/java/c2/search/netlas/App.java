package c2.search.netlas;

import c2.search.netlas.cli.Config;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Results;
import c2.search.netlas.target.Checker;
import c2.search.netlas.target.NetlasWrapper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

  public static void main(String[] args)
      throws ClassNotFoundException,
          InstantiationException,
          IllegalAccessException,
          NoSuchMethodException,
          InvocationTargetException {
    try {
      Options options = setupOptions();
      CommandLineParser parser = new DefaultParser();
      CommandLine cmd = parser.parse(options, args);

      if (cmd.hasOption("h")) {
        printHelp(options);
        return;
      }

      Host host = createHost(cmd);
      if (host == null) {
        printHelp(options);
        return;
      }

      Config config = new Config(CONFIG_FILENAME);
      String apiKey = config.get("api.key");
      if (apiKey == null) {
        LOGGER.error("API key is not set");
        return;
      }
      NetlasWrapper netlas = new NetlasWrapper(apiKey, host);
      Results responses = new Checker(netlas, host).run();
      responses.print(System.out);

    } catch (ParseException | IOException | NumberFormatException e) {
      LOGGER.error(e.getMessage());
      printHelp(setupOptions());
      System.exit(1);
    }
  }

  protected static Options setupOptions() {
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

    Option helpOption = Option.builder("h").longOpt("help").desc("Print this help message").build();

    Options options = new Options();
    options.addOption(setOption);
    options.addOption(getOption);
    options.addOption(targetOption);
    options.addOption(portOption);
    options.addOption(helpOption);

    return options;
  }

  protected static void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("c2detect", options);
  }

  protected static Host createHost(CommandLine cmd) {
    String domain = cmd.getOptionValue("t");
    if (domain == null) {
      LOGGER.error("Target domain is not specified");
      return null;
    }

    int port;
    try {
      String portStr = cmd.getOptionValue("p");
      port = Integer.parseInt(portStr);
    } catch (NumberFormatException e) {
      LOGGER.error("Invalid target port: " + cmd.getOptionValue("p"));
      return null;
    }

    return new Host(domain, port);
  }
}
