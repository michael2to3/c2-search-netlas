package c2.search.netlas.target;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.classscanner.ClassScanner;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CheckerTest {
  private Checker checker;
  private NetlasWrapper netlasWrapper;
  private Host host;

  @BeforeEach
  public void setUp() throws ClassNotFoundException, IOException {
    netlasWrapper = mock(NetlasWrapper.class);
    host = mock(Host.class);
    checker = new Checker(netlasWrapper, host);

    ClassScanner classScanner = mock(ClassScanner.class);
    when(classScanner.getClasses()).thenReturn(Arrays.asList(MyTarget.class));

    checker.setClassScanner(classScanner);
  }

  @Test
  public void testForEachTarget() throws Exception {
    var r = checker.run();
    var target = r.getResponses().get("My target");

    assertNotNull(r);
    assertTrue(target.size() >= 1);
    assertEquals(new Response(true), target.get(0));
  }

  @Test
  public void testThrowNotFoundClass() throws ClassNotFoundException, IOException {
    assertNotNull(checker.getHost());
    assertNotNull(Checker.getLogger());
    assertNotNull(checker.getClassScanner());
    assertNotNull(checker.getNetlasWrapper());
    assertNotNull(Checker.getTargetClassName());

    var host = new Host("localhost", 80);
    NetlasWrapper netlasWrapper = mock(NetlasWrapper.class);
    ClassScanner classScanner = mock(ClassScanner.class);
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          checker = new Checker(netlasWrapper, host);
          checker.setClassScanner(classScanner);
          checker.run();
        },
        "No detect class");
  }

  @Detect(name = "My target")
  public static class MyTarget {
    @Wire(name = "netlasWrapper")
    public NetlasWrapper netlasWrapper;

    @Wire(name = "host")
    public Host host;

    @c2.search.netlas.annotation.Test
    public Response method() {
      return new Response(host != null && netlasWrapper != null);
    }
  }
}
