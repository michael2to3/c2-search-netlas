package c2.search.netlas;

import c2.search.netlas.cli.Cli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    try {
      new Cli().run(System.out, args);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      System.exit(1);
    }
  }
}
