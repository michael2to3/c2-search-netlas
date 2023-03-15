package c2.search.netlas.scheme;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Results {
  protected static Map<String, List<Response>> sortBySuccessPercentage(
      Map<String, List<Response>> responses) {
    Map<String, Integer> successCount = new HashMap<>();
    for (Map.Entry<String, List<Response>> entry : responses.entrySet()) {
      int count = getSuccessPercentage(entry.getValue());
      successCount.put(entry.getKey(), count);
    }

    List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(successCount.entrySet());
    Collections.sort(
        sortedEntries,
        new Comparator<Map.Entry<String, Integer>>() {
          @Override
          public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
            return e2.getValue().compareTo(e1.getValue());
          }
        });

    Map<String, List<Response>> sortedResponses = new LinkedHashMap<>();
    for (Map.Entry<String, Integer> entry : sortedEntries) {
      sortedResponses.put(entry.getKey(), responses.get(entry.getKey()));
    }

    return sortedResponses;
  }

  private static int getSuccessCount(List<Response> toolResponses) {
    int numSuccess = 0;
    for (Response response : toolResponses) {
      if (response.isSuccess()) {
        numSuccess++;
      }
    }
    return numSuccess;
  }

  private static int getSuccessPercentage(List<Response> toolResponses) {
    int numSuccess = 0;
    for (Response response : toolResponses) {
      if (response.isSuccess()) {
        numSuccess++;
      }
    }
    return (int) ((double) numSuccess / toolResponses.size() * 100);
  }

  private Map<String, List<Response>> responses;

  public Results() {
    this.responses = new HashMap<>();
  }

  public Results(Map<String, List<Response>> responses) {
    this.responses = sortBySuccessPercentage(responses);
  }

  public void addResponse(String name, List<Response> responses) {
    this.responses.put(name, responses);
    this.responses = sortBySuccessPercentage(this.responses);
  }

  public void addResponse(String name, Response response) {
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

  public void setResponses(Map<String, List<Response>> responses) {
    this.responses = responses;
  }

  public void print(PrintStream stream) {
    printShort(stream);
  }

  public void print(PrintStream stream, boolean verbose) {
    if (verbose) {
      printVerbose(stream);
    } else {
      printShort(stream);
    }
  }

  public void printVerbose(PrintStream stream) {
    for (String tool : responses.keySet()) {
      List<Response> toolResponses = responses.get(tool);
      var resp = toolResponses.get(0);

      if (resp.getVersion() == null) {
        stream.printf("%-12s ", tool, resp.getVersion());
      } else {
        stream.printf("%-12s {Version: %s} ", tool, resp.getVersion());
      }

      printProgressBar(stream, getSuccessPercentage(toolResponses));
      stream.printf(" (%d/%d) ", getSuccessCount(toolResponses), toolResponses.size());

      for (Response response : toolResponses) {
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

  public void printShort(PrintStream stream) {
    for (String tool : responses.keySet()) {
      List<Response> toolResponses = responses.get(tool);
      if (getSuccessCount(toolResponses) == 0) {
        continue;
      }
      var resp = toolResponses.get(0);

      if (resp.getVersion().isEmpty()) {
        stream.printf("%-12s ", tool, resp.getVersion());
      } else {
        stream.printf("%-12s {Version: %s} ", tool, resp.getVersion());
      }
      printProgressBar(stream, getSuccessPercentage(toolResponses));
      stream.println();
    }
  }

  private void printProgressBar(PrintStream stream, int successPercentage) {
    StringBuilder progressBar = new StringBuilder("[");
    int numBlocks = successPercentage / 10;
    for (int i = 0; i < 10; i++) {
      if (i < numBlocks) {
        progressBar.append('#');
      } else {
        progressBar.append('X');
      }
    }
    progressBar.append("] ");
    progressBar.append(successPercentage).append("%");
    stream.print(progressBar);
  }
}
