package c2.search.netlas.scheme;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ResultTest {
  @Test
  void testPrint() {
    Map<String, List<Response>> responses = new HashMap<>();
    List<Response> moduleResponses1 = new ArrayList<>();
    moduleResponses1.add(new Response(true, new Version("1.0", "0.1"), "Description 1", "Error"));
    moduleResponses1.add(new Response(true, new Version("2.0", "0.2"), "Description 2", "Error"));
    List<Response> moduleResponses2 = new ArrayList<>();
    moduleResponses2.add(new Response(true, new Version("1.0", "0.1"), "Description 1", "Error"));
    moduleResponses2.add(new Response(true, new Version("2.0", "0.2"), "Description 2", "Error"));
    responses.put("Module 1", moduleResponses1);
    responses.put("Module 2", moduleResponses2);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    Results results = new Results(responses);
    results.print(System.out);

    System.setOut(new PrintStream(System.out));

    assertTrue(outputStream.toString().contains("Module 1"));
    assertTrue(outputStream.toString().contains("Module 2"));
  }

  @Test
  void testPrintShort() {
    Map<String, List<Response>> responses = new HashMap<>();
    List<Response> moduleResponses1 = new ArrayList<>();
    moduleResponses1.add(new Response(true, new Version("1.0", "0.1"), "Description 1", "Error"));
    moduleResponses1.add(new Response(true, new Version("2.0", "0.2"), "Description 2", "Error"));
    List<Response> moduleResponses2 = new ArrayList<>();
    moduleResponses2.add(new Response(true, new Version("1.0", "0.1"), "Description 1", "Error"));
    moduleResponses2.add(new Response(true, new Version("2.0", "0.2"), "Description 2", "Error"));
    responses.put("Module 1", moduleResponses1);
    responses.put("Module 2", moduleResponses2);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    Results results = new Results();
    results.setResponses(responses);
    results.printShort(System.out);

    assertTrue(outputStream.toString().contains("Module 1"));
    assertTrue(outputStream.toString().contains("Module 2"));

    results.print(System.out);
    assertTrue(outputStream.toString().contains("Module 1"));
    assertTrue(outputStream.toString().contains("Module 2"));
  }

  @Test
  void testPrintVerbose() {
    Map<String, List<Response>> responses = new HashMap<>();
    List<Response> moduleResponses1 = new ArrayList<>();
    moduleResponses1.add(new Response(true, new Version("1.0", "0.1"), "Description 1", "Error"));
    moduleResponses1.add(new Response(true, new Version("2.0", "0.2"), "Description 2", "Error"));
    List<Response> moduleResponses2 = new ArrayList<>();
    moduleResponses2.add(new Response(true, new Version("1.0", "0.1"), "Description 1", "Error"));
    moduleResponses2.add(new Response(true, new Version("2.0", "0.2"), "Description 2", "Error"));
    responses.put("Module 1", moduleResponses1);
    responses.put("Module 2", moduleResponses2);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    Results results = new Results();
    results.setResponses(responses);
    results.printVerbose(System.out);

    assertTrue(outputStream.toString().contains("Module 1"));
    assertTrue(outputStream.toString().contains("Module 2"));
  }

  @Test
  public void testSorted() {
    Map<String, List<Response>> responses = new HashMap<>();
    List<Response> moduleResponses1 = new ArrayList<>();
    moduleResponses1.add(new Response(true, new Version("1.0", "0.1"), "Description 1", "Error"));
    moduleResponses1.add(new Response(true, new Version("2.0", "0.2"), "Description 2", "Error"));
    List<Response> moduleResponses2 = new ArrayList<>();
    moduleResponses2.add(new Response(true, new Version("1.0", "0.1"), "Description 1", "Error"));
    moduleResponses2.add(new Response(true, new Version("2.0", "0.2"), "Description 2", "Error"));
    responses.put("Module 1", moduleResponses1);
    responses.put("Module 2", moduleResponses2);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    Results results = new Results(responses);
    results.print(System.out);

    int index1 = outputStream.toString().indexOf("Module 1");
    int index2 = outputStream.toString().indexOf("Module 2");

    assertTrue(index1 > index2);
  }

  @Test
  public void testHidePrint() {
    Map<String, List<Response>> responses = new HashMap<>();
    List<Response> moduleResponses1 = new ArrayList<>();
    moduleResponses1.add(new Response(true, new Version("1.0", "0.1"), "Description 1", "Error"));
    moduleResponses1.add(new Response(true, new Version("2.0", "0.2"), "Description 2", "Error"));
    List<Response> moduleResponses2 = new ArrayList<>();
    moduleResponses2.add(new Response(false, new Version("1.0", "0.1"), "Description 1", "Error"));
    moduleResponses2.add(new Response(false, new Version("2.0", "0.2"), "Description 2", "Error"));
    responses.put("Module 1", moduleResponses1);
    responses.put("Module 2", moduleResponses2);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    Results results = new Results(responses);
    results.print(System.out);

    assertTrue(outputStream.toString().contains("Module 1"));
    assertFalse(outputStream.toString().contains("Module 2"));
  }

  @Test
  public void testAddResponse() {
    Results results = new Results();
    results.addResponse("name", new Response(true, new Version("2.0", "0.2"), "Description 2", "Error"));
    results.addResponse("othername", new Response(true, new Version("2.0", "0.2"), "Description 2", "Error"));

    assertTrue(results.getResponses().containsKey("name"));
    assertTrue(results.getResponses().containsKey("othername"));
  }
}
