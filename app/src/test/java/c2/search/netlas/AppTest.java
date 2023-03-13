package c2.search.netlas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import c2.search.netlas.cli.Config;
import c2.search.netlas.scheme.Host;
import org.apache.commons.cli.CommandLine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AppTest {

  @BeforeAll
  public static void setup() {
    mockStatic(Config.class);
  }

  @Test
  public void testCreateHost() {
    CommandLine cmd = mock(CommandLine.class);
    when(cmd.getOptionValue("t")).thenReturn("example.com");
    when(cmd.getOptionValue("p")).thenReturn("8080");

    Host host = App.createHost(cmd);

    assertEquals("example.com", host.getTarget());
    assertEquals(8080, host.getPort());
  }

  @Test
  public void testCreateHostNull() {
    CommandLine cmd = mock(CommandLine.class);
    when(cmd.getOptionValue("t")).thenReturn(null);
    when(cmd.getOptionValue("p")).thenReturn("8080");

    Host host = App.createHost(cmd);

    assertNull(host);
  }
}
