package c2.search.netlas;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import c2.search.netlas.cli.CLArgumentsManager;
import c2.search.netlas.cli.Config;
import c2.search.netlas.scheme.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class AppTest {
  @Test
  void testPrintHelp() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    App.main(new String[] {"-h"});
    assertTrue(outputStream.toString().contains("c2detect"));
  }

  @Test
  void testChangeSettings() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);
    Config config = mock(Config.class, withSettings().useConstructor("test.prop"));
    App.setConfig(config);

    App.main(new String[] {"-s", "api"});

    verify(config).save("api.key", "api");
  }

  @Test
  void testC2DetectRunInvocationSelfSignCert() throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    String ip = "217.74.250.61";
    int port = 443;

    CLArgumentsManager cmd = mock(CLArgumentsManager.class);
    when(cmd.getApiKey()).thenReturn(System.getenv("API_KEY"));
    when(cmd.getHost()).thenReturn(Host.newBuilder().setTarget(ip).setPort(port).build());

    C2Detect c2detect = new C2Detect(cmd, printStream);
    c2detect.setCommandLineArgumentsManager(cmd);
    App.setC2detect(c2detect);

    String[] args = new String[] {"-t", ip, "-p", String.valueOf(port)};

    App.startScan(args);

    assertNotEquals("", printStream.toString());
  }

  @Test
  void testC2DetectRunInvocation() throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    String ip = "google.com";
    int port = 443;

    CLArgumentsManager cmd = mock(CLArgumentsManager.class);
    when(cmd.getApiKey()).thenReturn(System.getenv("API_KEY"));
    when(cmd.getHost()).thenReturn(Host.newBuilder().setTarget(ip).setPort(port).build());

    C2Detect c2detect = new C2Detect(cmd, printStream);
    c2detect.setCommandLineArgumentsManager(cmd);
    App.setC2detect(c2detect);

    String[] args = new String[] {"-t", ip, "-p", String.valueOf(port)};

    App.startScan(args);

    assertNotEquals("", printStream.toString());
  }

  @Test
  void testC2DetectWithoutSsl() throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    String ip = "neverssl.com";
    int port = 80;

    CLArgumentsManager cmd = mock(CLArgumentsManager.class);
    when(cmd.getApiKey()).thenReturn(System.getenv("API_KEY"));
    when(cmd.getHost()).thenReturn(Host.newBuilder().setTarget(ip).setPort(port).build());

    C2Detect c2detect = new C2Detect(cmd, printStream);
    c2detect.setCommandLineArgumentsManager(cmd);
    App.setC2detect(c2detect);

    String[] args = new String[] {"-t", ip, "-p", String.valueOf(port)};

    App.startScan(args);

    assertNotEquals("", printStream.toString());
  }

  @Test
  void testNotExistsArgs() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    String[] args = new String[] {"--not-exists-args"};
    App.main(args);

    assertTrue(outputStream.toString().contains("usage"));
  }

  @Test
  void testGettersAndSetters() {
    assertNotNull(App.getOut());
    assertNotNull(App.getConfigFilename());
    assertNotNull(App.getConfig());
    assertNotNull(App.getC2detect());
  }
}
