package c2.search.netlas.scheme;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Results {
  private static final String PRINT_VER = "%-12s ";
  private Map<String, List<Response>> responses;

  public Results() {
    this.responses = new HashMap<>();
  }

  public Results(final Map<String, List<Response>> responses) {
    this.responses = sortBySuccessPercentage(responses);
  }

  protected static Map<String, List<Response>> sortBySuccessPercentage(
      final Map<String, List<Response>> responses) {
    final Map<String, Integer> successCount = new ConcurrentHashMap<>();
    for (final Map.Entry<String, List<Response>> entry : responses.entrySet()) {
      final int count = getSuccessPercentage(entry.getValue());
      successCount.put(entry.getKey(), count);
    }

    final List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(successCount.entrySet());
    Collections.sort(
        sortedEntries,
        new Comparator<Map.Entry<String, Integer>>() {
          @Override
          public int compare(
              final Map.Entry<String, Integer> lhs, final Map.Entry<String, Integer> rhs) {
            return rhs.getValue().compareTo(lhs.getValue());
          }
        });

    final Map<String, List<Response>> sortedResponses = new ConcurrentHashMap<>();
    for (final Map.Entry<String, Integer> entry : sortedEntries) {
      sortedResponses.put(entry.getKey(), responses.get(entry.getKey()));
    }

    return sortedResponses;
  }

  private static int getSuccessCount(final List<Response> toolResponses) {
    int numSuccess = 0;
    for (final Response response : toolResponses) {
      if (response.isSuccess()) {
        numSuccess++;
      }
    }
    return numSuccess;
  }

  private static int getSuccessPercentage(final List<Response> toolResponses) {
    int numSuccess = 0;
    for (final Response response : toolResponses) {
      if (response.isSuccess()) {
        numSuccess++;
      }
    }
    return (int) ((double) numSuccess / toolResponses.size() * 100);
  }

  public void addResponse(final String name, final List<Response> responses) {
    this.responses.put(name, responses);
    this.responses = sortBySuccessPercentage(this.responses);
  }

  public void addResponse(final String name, final Response response) {
    List<Response> responses = this.responses.get(name);
    if (responses == null) {
      responses = new ArrayList<>();
      this.responses.put(name, responses);
    }
    responses.add(response);
  }

  public Map<String, List<Response>> getResponses() {
    return responses;
  }

  public void setResponses(final Map<String, List<Response>> responses) {
    this.responses = responses;
  }

  public void print(final PrintStream stream) {
    printShort(stream);
  }

  public void print(final PrintStream stream, final boolean verbose) {
    if (verbose) {
      printVerbose(stream);
    } else {
      printShort(stream);
    }
  }

  public void printVerbose(final PrintStream stream) {
    for (final String tool : responses.keySet()) {
      final List<Response> toolResponses = responses.get(tool);
      final var resp = toolResponses.get(0);

      if (resp.getVersion().isEmpty()) {
        stream.printf(PRINT_VER, tool, resp.getVersion());
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

  public void printShort(final PrintStream stream) {
    for (final String tool : responses.keySet()) {
      final List<Response> toolResponses = responses.get(tool);
      if (getSuccessCount(toolResponses) == 0) {
        continue;
      }
      final var resp = toolResponses.get(0);

      if (resp.getVersion().isEmpty()) {
        stream.printf(PRINT_VER, tool, resp.getVersion());
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
}
