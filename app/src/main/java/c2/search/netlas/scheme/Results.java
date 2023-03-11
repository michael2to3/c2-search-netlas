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
    for (String tool : responses.keySet()) {
      List<Response> toolResponses = responses.get(tool);
      stream.printf("%-12s {Version: %s} ", tool, toolResponses.get(0).getVersion());
      printProgressBar(getSuccessPercentage(toolResponses));
      stream.println();
    }
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
