package c2.search.netlas.classscanner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.target.NetlasWrapper;
import java.io.IOException;
import java.net.Socket;
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
    var fields = new FieldValues();
    fields.setField(Host.class, host);
    fields.setField(NetlasWrapper.class, netlasWrapper);
    fields.setField(Netlas.class, netlasWrapper.getNetlas());

    checker = new Checker(fields);
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
    var otarget = responses.get("c2.search.netlas.classscanner.CheckerTest$OtherTarget");

    assertNotNull(r);
    assertTrue(target.size() >= 1);
    assertTrue(otarget.size() >= 1);
    assertTrue(target.get(0).isSuccess());
    assertTrue(otarget.get(0).isSuccess());
  }

  @Test
  public void testThrowNotFoundClass() throws ClassNotFoundException, IOException {
    var host = new Host("localhost", 80);
    NetlasWrapper netlasWrapper = mock(NetlasWrapper.class);
    var fields = new FieldValues();
    fields.setField(Host.class, host);
    fields.setField(NetlasWrapper.class, netlasWrapper);
    ClassScanner classScanner = mock(ClassScanner.class);
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
    @Wire public Socket socket;
    public boolean isChangedBeforeAll;

    @c2.search.netlas.annotation.BeforeAll
    public void beforeAll() {
      isChangedBeforeAll = true;
    }

    @c2.search.netlas.annotation.Test
    public Response method() {
      return new Response(host != null && netlasWrapper != null);
    }

    @c2.search.netlas.annotation.Test
    public Response throwException() throws IOException {
      throw new IOException();
    }
  }
}
