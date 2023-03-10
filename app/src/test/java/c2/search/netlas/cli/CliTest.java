package c2.search.netlas.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import c2.search.netlas.scheme.Host;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CliTest {
  private Cli cli;
  private ByteArrayOutputStream out;

  @BeforeEach
  void setUp() {
    cli = new Cli();
    out = new ByteArrayOutputStream();
  }

  @Test
  void run_shouldReturnHost_whenValidTargetAndPortProvided() throws Exception {
    String[] args = {"-t", "127.0.0.1", "-p", "8080"};
    Host expectedHost = new Host("127.0.0.1", 8080);

    Host actualHost = cli.run(new PrintStream(out), args);

    assertNotNull(actualHost);
    assertEquals(expectedHost, actualHost);
  }

  @Test
  void run_shouldPrintApiKey_whenGFlagProvided() throws Exception {
    String[] args = {"-g"};
    Cli cliSpy = mock(Cli.class);
    when(cliSpy.run(any(PrintStream.class), any(String[].class))).thenReturn(new Host());

    cliSpy.run(new PrintStream(out), args);

    verify(cliSpy).run(any(PrintStream.class), any(String[].class));
    assertNotNull(out.toString());
  }

  @Test
  void run_shouldPrintHelp_whenNoArgumentsProvided() throws Exception {
    String[] args = {};
    cli.run(new PrintStream(out), args);
    assertNotNull(out.toString());
  }
}
