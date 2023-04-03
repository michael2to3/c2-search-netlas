package c2.search.netlas;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import c2.search.netlas.cli.Config;
import c2.search.netlas.cli.ParseCmdArgs;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class AppTest {
  @Test
  void printHelp() {
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
  void testC2DetectRunInvocation() throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    String[] args = new String[] {"-t", "example.com", "-p", "80"};
    ParseCmdArgs pargs = App.getParseCmdArgs(args);
    C2Detect c2Detect = spy(new C2Detect(pargs, printStream));
    doNothing().when(c2Detect).run();

    App.setC2Detect(c2Detect);
    App.startScan(args);

    verify(c2Detect).run(args);
  }
}
