package c2.search.netlas.scheme;

import java.io.PrintStream;
import java.util.List;

public class ResultsPrinter {
  private final Results results;

  public ResultsPrinter(final Results results) {
    this.results = results;
  }

  public void print(final PrintStream stream, final boolean verbose) {
    if (isEmptyResults()) {
      stream.println("Detection results are empty");
    } else if (verbose) {
      printVerbose(stream);
    } else {
      printShort(stream);
    }
  }

  private boolean isEmptyResults() {
    if (results.getResponses().isEmpty()) {
      return true;
    }
    for(final String tool : results.getResponses().keySet()) {
      if (!results.getResponses().get(tool).isEmpty()) {
        return false;
      }
    }
    return true;
  }

  private void printVerbose(final PrintStream stream) {
    for (final String tool : results.getResponses().keySet()) {
      final List<Response> toolResponses = results.getResponses().get(tool);
      if(getSuccessCount(toolResponses) == 0) {
        continue;
      }
      final Response resp = toolResponses.get(0);

      if (resp.getVersion().isEmpty()) {
        stream.printf("%-12s ", tool);
      } else {
        stream.printf("%-12s {Version: %s} ", tool, resp.getVersion());
      }

      printProgressBar(stream, getSuccessPercentage(toolResponses));
      stream.printf(" (%d/%d) ", getSuccessCount(toolResponses), toolResponses.size());

      for (final Response response : toolResponses) {
        if (response.isSuccess()
            && response.getDescription() != null
            && !response.getDescription().isEmpty()) {
          stream.printf("\n Test successful: %s", response.getDescription());
        }
        if (!response.getError().isEmpty()) {
          stream.print("\n\r  Error: " + response.getError());
        }
      }

      stream.println();
    }
  }

  private void printShort(final PrintStream stream) {
    for (final String tool : results.getResponses().keySet()) {
      final List<Response> toolResponses = results.getResponses().get(tool);
      if (getSuccessCount(toolResponses) == 0) {
        continue;
      }
      final Response resp = toolResponses.get(0);

      if (resp.getVersion().isEmpty()) {
        stream.printf("%-12s ", tool);
      } else {
        stream.printf("%-12s {Version: %s} ", tool, resp.getVersion());
      }
      printProgressBar(stream, getSuccessPercentage(toolResponses));
      stream.println();
    }
  }

  private void printProgressBar(final PrintStream stream, final int successPercentage) {
    final int len = 40;
    final double max = 100.0;
    final String symbol = "█";
    final int numBlocks = (int) Math.ceil((successPercentage / max) * len);

    final StringBuilder progress = new StringBuilder();
    progress.append('[');
    for (int i = 0; i < numBlocks; i++) {
      progress.append(symbol);
    }
    for (int i = numBlocks; i < len; i++) {
      progress.append(' ');
    }
    progress.append("] ").append(successPercentage).append('%');

    stream.print(progress.toString());
  }

  private static long getSuccessCount(final List<Response> toolResponses) {
    return toolResponses.stream().filter(Response::isSuccess).count();
  }

  private static int getSuccessPercentage(final List<Response> toolResponses) {
    int numSuccess = 0;
    for (final Response response : toolResponses) {
      if (response.isSuccess()) {
        numSuccess++;
      }
    }
    final int total = 100;
    return (int) ((double) numSuccess / toolResponses.size() * total);
  }
}
