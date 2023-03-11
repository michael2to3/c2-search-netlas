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
    for (Map.Entry<String, List<Response>> entry : responses.entrySet()) {
      String moduleName = entry.getKey();
      List<Response> moduleResponses = entry.getValue();

      int totalResponses = moduleResponses.size();
      int successfulResponses = 0;
      for (Response response : moduleResponses) {
        if (response.isSuccess()) {
          successfulResponses++;
        }
      }
      int successPercentage = (int) Math.round((double) successfulResponses / totalResponses * 100);

      StringBuilder progressBar = new StringBuilder("[");
      int progress = 0;
      for (int i = 0; i < 10; i++) {
        if (progress < successPercentage) {
          progressBar.append("#");
        } else {
          progressBar.append("X");
        }
        progress += 10;
      }
      progressBar.append("]");

      stream.printf("%-15s %s %d%%%n", moduleName, progressBar.toString(), successPercentage);
    }
  }
}
