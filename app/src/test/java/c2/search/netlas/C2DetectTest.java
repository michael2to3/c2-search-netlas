package c2.search.netlas;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

import c2.search.netlas.cli.Config;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.Results;
import c2.search.netlas.scheme.Version;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class C2DetectTest {
  private static final String API = new Config("config.properties").get("api.key");

  @BeforeAll
  public static void setup() {
    mockStatic(Config.class);
  }

  @Test
  public void testPrintHelp() {
    String[] args = new String[] {"--help"};

    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    App.main(args);

    assertTrue(outContent.toString().contains("-h"));
  }

  private Results getResults() {
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
    return results;
  }
}
