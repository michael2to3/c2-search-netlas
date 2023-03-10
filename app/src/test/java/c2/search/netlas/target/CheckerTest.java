package c2.search.netlas.target;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import c2.search.netlas.annotation.*;
import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.classscanner.ClassScanner;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import java.io.IOException;
import java.util.Arrays;
import netlas.java.Netlas;
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
    checker.setHost(host);
    checker.setNetlasWrapper(netlasWrapper);

    ClassScanner classScanner = mock(ClassScanner.class);
    when(classScanner.getClassesWithAnnotation(Detect.class))
        .thenReturn(Arrays.asList(MyTarget.class, OtherTarget.class));

    checker.setClassScanner(classScanner);
  }

  @Test
  public void testForEachTarget() throws Exception {
    var r = checker.run();
    var responses = r.getResponses();
    assertNotNull(responses);

    var target = responses.get("My target");
    var otarget = responses.get("c2.search.netlas.target.CheckerTest$OtherTarget");

    assertNotNull(r);
    assertTrue(target.size() >= 1);
    assertTrue(otarget.size() >= 1);
    assertEquals(new Response(true), target.get(0));
    assertEquals(new Response(true), otarget.get(0));
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
        IllegalStateException.class,
        () -> {
          checker = new Checker(netlasWrapper, host);
          checker.setClassScanner(classScanner);
          checker.run();
        },
        "No detect class");
  }

  @Detect
  public static class OtherTarget {
    @Wire public NetlasWrapper netlasWrapper;

    @Wire public Host host;

    @c2.search.netlas.annotation.Test
    public Response method() {
      return new Response(host != null && netlasWrapper != null);
    }
  }

  @Detect(name = "My target")
  public static class MyTarget {
    @Wire public NetlasWrapper netlasWrapper;

    @Wire public Netlas netlas;

    @Wire public Host host;

    @c2.search.netlas.annotation.Test
    public Response method() {
      return new Response(host != null && netlasWrapper != null);
    }
  }
}
