package c2.search.netlas;

import c2.search.netlas.cli.Cli;
import c2.search.netlas.cli.Config;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Results;
import c2.search.netlas.target.Checker;
import c2.search.netlas.target.NetlasWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    try {
      Host host = new Cli().run(System.out, args);

      if (host == null) {
        return;
      }

      var config = new Config("config.properties");
      var api = config.get("api.key");
      var netlas = new NetlasWrapper(api, host);
      Results reponses = new Checker(netlas, host).run();
      reponses.print(System.out);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      System.exit(1);
    }
  }
}
