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

    host = new Host();
    host.setTarget("www.yahoo.com");
    host.setPort(8080);
    assertEquals("www.yahoo.com", host.getTarget()); 
    assertEquals(8080, host.getPort());
  }
}
