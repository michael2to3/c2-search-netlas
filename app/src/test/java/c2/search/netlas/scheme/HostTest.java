package c2.search.netlas.scheme;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HostTest {
  @Test
  void testGetterAndSetter() {
    Host host = new Host("www.google.com", 80);
    assertEquals("www.google.com", host.getTarget());
    host.setTarget("www.yahoo.com");
    assertEquals("www.yahoo.com", host.getTarget());
    host.setPort(8080);
    assertEquals(8080, host.getPort());

    host = Host.newBuilder().setTarget("www.yahoo.com").setPort(8080).setPath("/").build();

    Host other = new Host("www.yahoo.com", 8080);
    assertEquals(other, other);
    assertEquals(host, other);
    assertNotEquals(host, new String());
    assertEquals(host.toString(), other.toString());
    assertEquals(host.hashCode(), other.hashCode());
    assertNotEquals(null, host);
    assertNotEquals(host.getTarget(), host);
  }

  @Test
  void testToString() {
    Host host = new Host("8.8.8.8", 53);
    assertEquals("8.8.8.8:53", host.toString());
  }
}
