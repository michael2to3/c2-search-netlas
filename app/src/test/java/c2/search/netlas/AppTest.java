package c2.search.netlas;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.junit.jupiter.api.Test;

class AppTest {

  @Test
  void printHelp() {
    CommandLine mockCmd = mock(CommandLine.class);
    when(mockCmd.hasOption("h")).thenReturn(true);

    App.main(new String[] {"-h"});

    HelpFormatter formatter = new HelpFormatter();
    verify(formatter).printHelp("c2detect", App.setupOptions());
  }
}
