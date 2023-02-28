package c2.search.netlas;

import org.apache.commons.cli.*;

public class App {
  public static void main(String[] args) {
    Options options = new Options();

    OptionGroup configGroup = new OptionGroup();
    configGroup.addOption(Option.builder()
        .longOpt("set")
        .argName("APIHASH")
        .hasArg()
        .desc("Set the API hash")
        .build());
    configGroup.addOption(Option.builder()
        .longOpt("get")
        .desc("Get the API hash")
        .build());
    options.addOptionGroup(configGroup);

    options.addOption(Option.builder("t")
        .longOpt("target")
        .argName("URL")
        .hasArg()
        .desc("The target URL")
        .build());

    options.addOption(Option.builder()
        .longOpt("timeout")
        .argName("SECONDS")
        .hasArg()
        .desc("The timeout in seconds")
        .build());

    CommandLineParser parser = new DefaultParser();

    try {
      CommandLine cmd = parser.parse(options, args);
      if (cmd.hasOption("set")) {
        String apiHash = cmd.getOptionValue("set");

      } else if (cmd.hasOption("get")) {

      }

      if (cmd.hasOption("target")) {
        String targetUrl = cmd.getOptionValue("target");
      }

      if (cmd.hasOption("timeout")) {
        int timeout = Integer.parseInt(cmd.getOptionValue("timeout"));

      }
    } catch (ParseException e) {
      System.err.println("Error: " + e.getMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("MyProgram", options);
      System.exit(1);
    }
  }
}
