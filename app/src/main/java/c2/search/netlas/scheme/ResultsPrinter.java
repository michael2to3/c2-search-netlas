package c2.search.netlas.scheme;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultsPrinter {
  private final Logger LOGGER = LoggerFactory.getLogger(ResultsPrinter.class);
  private final Results results;

  public ResultsPrinter(final Results results) {
    this.results = results;
  }

  public void print(final PrintStream stream, final boolean verbose, final boolean json) {
    if (verbose) {
      printVerbose(stream);
    } else if (json) {
      printJson(stream);
    } else {
      printShort(stream);
    }
  }

  private void printJson(final PrintStream stream) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      mapper.writeValue(stream, results);
    } catch (IOException e) {
      LOGGER.error("Failed to write JSON", e);
    }
  }

  private void printVerbose(final PrintStream stream) {
    for (final String tool : results.getResponses().keySet()) {
      final List<Response> toolResponses = results.getResponses().get(tool);
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
