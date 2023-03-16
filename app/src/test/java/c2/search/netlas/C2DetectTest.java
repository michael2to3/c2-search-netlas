package c2.search.netlas;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import c2.search.netlas.cli.*;
import c2.search.netlas.scheme.*;
import c2.search.netlas.scheme.Host;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.apache.commons.cli.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class C2DetectTest {
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

  @Test
  public void testChangeSettings() throws Exception {
    String args[] = new String[] {"-s", "test.test"};
    Config configMock = mock(Config.class);
    C2Detect c2 = new C2Detect(configMock, App.setupOptions());
    c2.run(args);
    verify(configMock).save("api.key", "test.test");
  }

  @Test
  public void testWithoutApi() throws Exception {
    String args[] = new String[] {"-v", "-t", "google.com", "-p", "443"};
    Config configMock = mock(Config.class);
    when(configMock.get("api.key")).thenReturn(null);
    C2Detect c2 = new C2Detect(configMock, App.setupOptions());
    assertThrows(Exception.class, () -> c2.run(args));
  }
}
