package c2.search.netlas;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import c2.search.netlas.cli.CLArgumentsManager;
import c2.search.netlas.scheme.Host;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  @BeforeEach
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
    App.setConfigFileName("test.properties");
  }

  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  void testHelpOption() {
    App.main(new String[] {"-h"});
    assertTrue(outContent.toString().contains("usage: c2detect"));
  }

  @Test
  void testSetApiKey() {
    App.main(new String[] {"-s", "testApiKey"});
    assertTrue(outContent.toString().isEmpty());
  }

  @Test
  void testInvokeWithoutExplicitApiKey() {
    App.main(new String[] {"-s", System.getenv("API_KEY")});
    var args = new String[] {"-t", "google.com", "-p", "443"};
    App.main(args);
    assertNotNull(outContent.toString());
  }

  @Test
  void testVerboseOption() throws ParseException {
    CLArgumentsManager clArgumentsManager = mock(CLArgumentsManager.class);
    when(clArgumentsManager.isChangeApiKey()).thenReturn(true);
    when(clArgumentsManager.getApiKey()).thenReturn(System.getenv("API_KEY"));
    when(clArgumentsManager.isVerbose()).thenReturn(true);
    when(clArgumentsManager.getTarget())
        .thenReturn(Host.newBuilder().setTarget("google.com").setPort(443).build());
    when(clArgumentsManager.isChangeTarget()).thenReturn(true);
    when(clArgumentsManager.isChangePort()).thenReturn(true);
    when(clArgumentsManager.isHelp()).thenReturn(false);
    when(clArgumentsManager.isInvalid()).thenReturn(false);

    String[] args = new String[] {"-t", "google.com", "-p", "443", "-v"};

    App.initialize(args);
    App.setCLArgumentsManager(clArgumentsManager);
    App.runApp(args);
    assertNotEquals("", outContent.toString());
  }

  @Test
  void testShortOutput() throws ParseException {
    CLArgumentsManager clArgumentsManager = mock(CLArgumentsManager.class);
    when(clArgumentsManager.isChangeApiKey()).thenReturn(true);
    when(clArgumentsManager.getApiKey()).thenReturn(System.getenv("API_KEY"));
    when(clArgumentsManager.isVerbose()).thenReturn(false);
    when(clArgumentsManager.getTarget())
        .thenReturn(Host.newBuilder().setTarget("google.com").setPort(443).build());
    when(clArgumentsManager.isChangeTarget()).thenReturn(true);
    when(clArgumentsManager.isChangePort()).thenReturn(true);
    when(clArgumentsManager.isHelp()).thenReturn(false);
    when(clArgumentsManager.isInvalid()).thenReturn(false);

    String[] args = new String[] {"-t", "google.com", "-p", "443"};

    App.initialize(args);
    App.setCLArgumentsManager(clArgumentsManager);
    App.runApp(args);
    assertNotNull(outContent.toString());
  }

  @Test
  void testSetApiKey2() throws ParseException {
    App.initialize(new String[] {"-s", "new_api_key"});
    assertEquals("new_api_key", App.getCLArgumentsManager().getApiKey());
  }

  @Test
  void testSetApiKeyAndTarget() throws ParseException {
    var args = new String[] {"-s", "new_api_key", "-t", "google.com"};
    App.initialize(args);
    assertEquals("new_api_key", App.getCLArgumentsManager().getApiKey());
  }
}
