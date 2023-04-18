package c2.search.netlas.scheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Results {
  private Map<String, List<Response>> responses;

  public Results() {
    this.responses = new HashMap<>();
  }

  public Results(final Map<String, List<Response>> responses) {
    this.responses = responses;
  }

  public void merge(final Results results) {
    for (final Map.Entry<String, List<Response>> entry : results.responses.entrySet()) {
      addResponse(entry.getKey(), entry.getValue());
    }
  }

  public void addResponse(final String name, final List<Response> responses) {
    this.responses.put(name, responses);
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
}
