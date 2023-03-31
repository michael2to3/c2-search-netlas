package c2.search.netlas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

import c2.search.netlas.classscanner.AnnotatedFieldValues;
import c2.search.netlas.classscanner.Checker;
import c2.search.netlas.cli.Config;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.Results;
import c2.search.netlas.scheme.Version;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class C2DetectTest {
  private static final String API = new Config("config.properties").get("api.key");

  @BeforeAll
  public static void setup() {
    mockStatic(Config.class);
  }

  @Test
  public void testCreateHost() {
    CommandLine cmd = mock(CommandLine.class);
    when(cmd.getOptionValue("t")).thenReturn("example.com");
    when(cmd.getOptionValue("p")).thenReturn("8080");

    Host host = C2Detect.createHost(cmd);

    assertNotNull(host);
    assertEquals("example.com", host.getTarget());
    assertEquals(8080, host.getPort());

    when(cmd.getOptionValue("t")).thenReturn(null);
    assertThrows(IllegalArgumentException.class, () -> C2Detect.createHost(cmd));

    when(cmd.getOptionValue("t")).thenReturn("google.com");
    when(cmd.getOptionValue("p")).thenReturn("asd");
    assertThrows(IllegalArgumentException.class, () -> C2Detect.createHost(cmd));
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

  @Test
  public void testChangeSettings() throws Exception {
    String args[] = new String[] {"-s", "test.test"};
    Config configMock = mock(Config.class);
    C2Detect c2 = new C2Detect(configMock, App.setupOptions(), System.out);
    Checker checker = mock(Checker.class);
    when(checker.run()).thenReturn(getResults());

    c2.run(args);
    verify(configMock).save("api.key", "test.test");
  }

  @Test
  public void testWithoutApi() throws Exception {
    String args[] = new String[] {"-v", "-t", "google.com", "-p", "443"};
    Config configMock = mock(Config.class);
    when(configMock.get("api.key")).thenReturn(null);
    C2Detect c2 = new C2Detect(configMock, App.setupOptions(), System.out);
    assertThrows(Exception.class, () -> c2.run(args));
  }

  @Test
  public void testWithApi() throws Exception {
    String args[] = new String[] {"-v", "-t", "google.com", "-p", "443"};
    Config configMock = mock(Config.class);
    when(configMock.get("api.key")).thenReturn(API);

    Checker checker = mock(Checker.class);
    when(checker.run()).thenReturn(getResults());

    C2Detect c2 = new C2Detect(configMock, App.setupOptions(), System.out);
    c2.setChecker(checker);

    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    c2.run(args);
    verify(checker).run();
  }
}
