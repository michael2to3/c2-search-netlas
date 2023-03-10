package c2.search.netlas.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class OptionsCmd {
  public static Options get() {
    Option setOption =
        Option.builder("s")
            .longOpt("set")
            .hasArg()
            .argName("API")
            .desc("Set the API to use for the application")
            .build();

    Option getOption =
        Option.builder("g")
            .longOpt("get")
            .hasArg()
            .argName("OUTPUT")
            .desc("Get the configuration for the specified output")
            .build();

    Option targetOption =
        Option.builder("t")
            .longOpt("target")
            .hasArg()
            .argName("TARGET")
            .desc("Set the target domain for the application")
            .build();
    Option targetPortOption =
        Option.builder("p")
            .longOpt("port")
            .hasArg()
            .argName("PORT")
            .desc("Set the target port for the application")
            .build();
    Option enableDebugOption =
        Option.builder("debug")
            .longOpt("debug")
            .argName("ENABLE_DEBUG")
            .desc("Enable debug mode")
            .build();

    Options optionsWithConfig = new Options();
    optionsWithConfig.addOption(setOption);
    optionsWithConfig.addOption(getOption);
    optionsWithConfig.addOption(targetOption);
    optionsWithConfig.addOption(targetPortOption);
    optionsWithConfig.addOption(enableDebugOption);

    return optionsWithConfig;
  }
}
