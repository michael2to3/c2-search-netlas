package c2.search.netlas.target;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.classscanner.ClassScanner;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CheckerTest {
  private Checker checker;
  private NetlasWrapper netlasWrapper;
  private Host host;

  @BeforeEach
  public void setUp() {
    netlasWrapper = mock(NetlasWrapper.class);
    host = mock(Host.class);
    checker = new Checker(netlasWrapper, host);
  }

  @Test
  public void testForEachTarget() throws Exception {
    ClassScanner classScanner = mock(ClassScanner.class);
    when(classScanner.getClasses()).thenReturn(Arrays.asList(MyTarget.TestClass.class));

    checker.run();

    assertEquals(new Response(true), new MyTarget.TestClass().testMethod());
  }

  @Detect(name = "My target")
  public static class MyTarget {
    public static class TestClass {
      @Wire(name = "netlasWrapper")
      public NetlasWrapper netlasWrapper;

      @Wire(name = "host")
      public Host host;

      @Test
      public Response testMethod() {
        return new Response(true);
      }
    }
  }
}
