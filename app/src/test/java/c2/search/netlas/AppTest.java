package c2.search.netlas;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
  void testVerboseOption() {
    App.main(new String[] {"-t", "example.com", "-v"});
    assertNotEquals("", outContent.toString());
  }
}
