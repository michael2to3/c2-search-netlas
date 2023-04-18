package c2.search.netlas.cli;

import java.io.OutputStream;
import java.io.PrintStream;

public class OutputHandler extends PrintStream {
  public OutputHandler() {
    super(System.out);
  }

  public OutputHandler(final OutputStream out) {
    super(out);
  }
}
