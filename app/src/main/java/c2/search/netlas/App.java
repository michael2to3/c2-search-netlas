package c2.search.netlas;

import c2.search.netlas.cli.Config;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
  private static final String CONFIG_FILENAME = "config.properties";

  public static void main(String[] args) {
    Config config = new Config(CONFIG_FILENAME);
    C2Detect c2Detect = new C2Detect(config, setupOptions());
    c2Detect.run(args);
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
