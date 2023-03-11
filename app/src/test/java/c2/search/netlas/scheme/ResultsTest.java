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
    moduleResponses1.add(new Response(true, new Version("1.0", "0.1"), "Description 1", null));
    moduleResponses1.add(new Response(false, null, null, "Error 1"));
    moduleResponses1.add(new Response(true, new Version("2.0", "0.2"), "Description 2", null));
    List<Response> moduleResponses2 = new ArrayList<>();
    moduleResponses2.add(new Response(false, null, null, "Error 2"));
    moduleResponses2.add(new Response(false, null, null, "Error 3"));
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
}
