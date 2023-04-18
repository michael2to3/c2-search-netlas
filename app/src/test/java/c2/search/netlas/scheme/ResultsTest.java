package c2.search.netlas.scheme;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResultsTest {

  private Results results;

  @BeforeEach
  void setUp() {
    results = new Results();
  }

  @Test
  void testMerge() {
    Results resultsToMerge = new Results();
    Response response = Response.newBuilder().build();
    resultsToMerge.addResponse("TestName", response);

    results.merge(resultsToMerge);

    Map<String, List<Response>> expectedResponses = new HashMap<>();
    List<Response> responseList = new ArrayList<>();
    responseList.add(response);
    expectedResponses.put("TestName", responseList);

    assertEquals(expectedResponses, results.getResponses());
  }

  @Test
  void testAddResponseWithList() {
    List<Response> responseList = new ArrayList<>();
    Response response = Response.newBuilder().build();
    responseList.add(response);

    results.addResponse("TestName", responseList);

    Map<String, List<Response>> expectedResponses = new HashMap<>();
    expectedResponses.put("TestName", responseList);

    assertEquals(expectedResponses, results.getResponses());
  }

  @Test
  void testAddResponseWithSingleResponse() {
    Response response = Response.newBuilder().build();
    results.addResponse("TestName", response);

    Map<String, List<Response>> expectedResponses = new HashMap<>();
    List<Response> responseList = new ArrayList<>();
    responseList.add(response);
    expectedResponses.put("TestName", responseList);

    assertEquals(expectedResponses, results.getResponses());
  }
}
