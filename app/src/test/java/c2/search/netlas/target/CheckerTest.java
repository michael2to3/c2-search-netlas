package c2.search.netlas.target;

import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

public class CheckerTest {

  @Mock private NetlasWrapper netlasWrapper;
  @Mock private Host host;
  @Mock private Logger logger;

  private Checker checker;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    checker = new Checker(netlasWrapper, host);
    setLogger();
  }

  private void setLogger() {
    try {
      Field loggerField = Checker.class.getDeclaredField("LOGGER");
      loggerField.setAccessible(true);
      loggerField.set(null, logger);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private static class DummyTarget {
    @Wire private NetlasWrapper netlasWrapper;

    @Wire(name = "host")
    private Host host;

    @Test
    public Response dummyTestMethod() {
      return new Response(false);
    }
  }
}
