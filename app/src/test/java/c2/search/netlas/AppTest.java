package c2.search.netlas;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
}
