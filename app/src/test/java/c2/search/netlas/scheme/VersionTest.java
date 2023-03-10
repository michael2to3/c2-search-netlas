package c2.search.netlas.scheme;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class VersionTest {
  @Test
  void testGetterAndSetter() {
    Version version = new Version("1.0", "0.1");
    assertEquals("1.0", version.getMax());
    assertEquals("0.1", version.getMin());
    version.setMax("2.0");
    version.setMin("1.0");
    assertEquals("2.0", version.getMax());
    assertEquals("1.0", version.getMin());

    version = new Version();
    version.setMax("2.0");
    version.setMin("1.0");
    assertEquals("2.0", version.getMax());
    assertEquals("1.0", version.getMin());
  }
}
