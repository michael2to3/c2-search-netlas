package c2.search.netlas.broadcast;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class NormalizeRangeHostTest {
  @Test
  void testNormalizeValidRange() throws IpRangeFormatException {
    String inputRange = "192.168.0.1-192.168.0.10";
    NormalizeRangeHost normalizeRangeHost = new NormalizeRangeHost(inputRange);

    String expectedNormalizedRange = "[192.168.0.1 TO 192.168.0.10]";
    String actualNormalizedRange = normalizeRangeHost.normalize();

    assertEquals(expectedNormalizedRange, actualNormalizedRange);
  }

  @Test
  void testNormalizeValidRangeWithSpaces() throws IpRangeFormatException {
    String inputRange = "192.168.0.1 - 192.168.0.10";
    NormalizeRangeHost normalizeRangeHost = new NormalizeRangeHost(inputRange);

    String expectedNormalizedRange = "[192.168.0.1 TO 192.168.0.10]";
    String actualNormalizedRange = normalizeRangeHost.normalize();

    assertEquals(expectedNormalizedRange, actualNormalizedRange);
  }

  @Test
  void testNormalizeValidRangeWithComma() throws IpRangeFormatException {
    String inputRange = "192.168.0.1,192.168.0.10";
    NormalizeRangeHost normalizeRangeHost = new NormalizeRangeHost(inputRange);

    String expectedNormalizedRange = "[192.168.0.1 TO 192.168.0.10]";
    String actualNormalizedRange = normalizeRangeHost.normalize();

    assertEquals(expectedNormalizedRange, actualNormalizedRange);
  }

  @Test
  void testNormalizeInvalidRange() {
    String inputRange = "192.168.0.300-192.168.0.10";
    NormalizeRangeHost normalizeRangeHost = new NormalizeRangeHost(inputRange);

    assertThrows(IpRangeFormatException.class, normalizeRangeHost::normalize);
  }

  @Test
  void testNormalizeValidSubnet() throws IpRangeFormatException {
    String inputRange = "192.168.0.1/24";
    NormalizeRangeHost normalizeRangeHost = new NormalizeRangeHost(inputRange);

    String expectedNormalizedRange = "\"192.168.0.1/24\"";
    String actualNormalizedRange = normalizeRangeHost.normalize();

    assertEquals(expectedNormalizedRange, actualNormalizedRange);
  }

  @Test
  void testNormalizeInvalidSubnet() {
    String inputRange = "192.168.0.300/24";
    NormalizeRangeHost normalizeRangeHost = new NormalizeRangeHost(inputRange);

    assertThrows(IpRangeFormatException.class, normalizeRangeHost::normalize);
  }
}
