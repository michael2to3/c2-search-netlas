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

    version = new Version("1.0", "0.1");
    version.setMax("2.0");
    version.setMin("1.0");
    assertEquals("2.0", version.getMax());
    assertEquals("1.0", version.getMin());
  }

  @Test
  void testEquals() {
    Version version1 = new Version("1.0", "0.1");
    Version version2 = new Version("1.0", "0.1");
    assertEquals(version1, version2);
    assertEquals(version1.hashCode(), version2.hashCode());
    assertEquals(version1.toString(), version2.toString());
    assertNotEquals(version1, null);
    assertNotEquals(version1, "");
    assertNotEquals(version1, "1.0");
    assertNotEquals(version1, 1.0);
    assertNotEquals(version1, new Object());
    assertNotEquals(version1, new Version("1.1", "0.1"));
    version1 = new Version("", "");
    assertTrue(version1.isEmpty());
    version1 = new Version(null, null);
    assertTrue(version1.isEmpty());
    version1 = new Version(null, "");
    assertTrue(version1.isEmpty());
    version1 = new Version("", null);
    assertTrue(version1.isEmpty());
  }

  @Test
  void testCompare() {
    Version version1 = new Version("1.0", "0.1");
    Version version2 = new Version("1.0", "0.2");
    assertTrue(version1.compareTo(version2) < 0);
    assertTrue(version2.compareTo(version1) > 0);
    assertTrue(version1.compareTo(version1) == 0);
    assertTrue(version2.compareTo(version2) == 0);
    assertTrue(version1.compareTo(new Version("1.0", "0.1")) == 0);
  }
}
