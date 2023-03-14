package c2.search.netlas.scheme;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Results {
  private Map<String, List<Response>> responses;

  public Results() {
    this.responses = new HashMap<>();
  }

  public Results(Map<String, List<Response>> responses) {
    this.responses = responses;
  }

  public void addResponse(String name, List<Response> responses) {
    this.responses.put(name, responses);
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

      printProgressBar(getSuccessPercentage(toolResponses));
      stream.print(" ");

      stream.printf("(%d/%d)", toolResponses.size(), getSuccessCount(toolResponses));
      stream.print(" ");

      for (Response response : toolResponses) {
        if (response.getDescription() != "")
          stream.print("\n  Description: " + response.getDescription());
        if (response.getError() != "") stream.print("\n  Error: " + response.getError());
      }

      stream.println();
    }
  }

  private int getSuccessCount(List<Response> toolResponses) {
    int numSuccess = 0;
    for (Response response : toolResponses) {
      if (response.isSuccess()) {
        numSuccess++;
      }
    }
    return numSuccess;
  }

  private void printProgressBar(int successPercentage) {
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
    System.out.print(progressBar);
  }

  public void printShort(PrintStream stream) {
    for (String tool : responses.keySet()) {
      List<Response> toolResponses = responses.get(tool);
      var resp = toolResponses.get(0);

      if (resp.getVersion() == null) {
        stream.printf("%-12s ", tool, resp.getVersion());
      } else {
        stream.printf("%-12s {Version: %s} ", tool, resp.getVersion());
      }

      printProgressBar(getSuccessPercentage(toolResponses));
      stream.println();
    }
  }

  private int getSuccessPercentage(List<Response> toolResponses) {
    int numSuccess = 0;
    for (Response response : toolResponses) {
      if (response.isSuccess()) {
        numSuccess++;
      }
    }
    return (int) ((double) numSuccess / toolResponses.size() * 100);
  }
}
