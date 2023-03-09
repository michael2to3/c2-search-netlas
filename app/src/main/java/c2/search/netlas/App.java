package c2.search.netlas;

import c2.search.netlas.cli.Cli;
import java.io.IOException;

public class App {

  public static void main(String[] args) {
    try {
      new Cli().run(System.out, args);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
