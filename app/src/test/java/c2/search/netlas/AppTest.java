package c2.search.netlas;

import static com.github.stefanbirkner.systemlambda.SystemLambda.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AppTest {
  @Mock private CommandLineParser mockParser;

  private ByteArrayOutputStream outContent;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    outContent = new ByteArrayOutputStream();
  }

  @Test
  void testMainWithHelpArg() throws Exception {
    String[] args = {"-h"};
    CommandLine mockCmd = mock(CommandLine.class);
    when(mockCmd.hasOption("h")).thenReturn(true);
    when(mockParser.parse(any(Options.class), eq(args))).thenReturn(mockCmd);

    App app =
        new App() {
          @Override
          protected static CommandLineParser getDefaultParser() {
            return mockParser;
          }
        };

    try (MockedPrintStream printStream = new MockedPrintStream(outContent)) {
      tapSystemOut(
          printStream,
          () -> {
            int exitStatus = catchSystemExit(() -> app.main(args));
            assertEquals(0, exitStatus);
          });
    }

    String output = outContent.toString();
    assertHelpMessageShown(output);
  }

  private void assertHelpMessageShown(String output) {
    // Verify that the help message is printed
    assertEquals(true, output.contains("usage: c2detect"));
    assertEquals(
        true,
        output.contains("-s,--set <API_KEY>        Set the API key to use for the application"));
  }

  // Add more test cases for other command line options and error scenarios.

  private static class MockedPrintStream extends PrintStream {
    MockedPrintStream(ByteArrayOutputStream outContent) {
      super(outContent, true);
    }
  }
}
