package c2.search.netlas.scheme;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ResponseTest {
  @Test
  void testGetterAndSetter() {
    Version version = new Version("1.0", "0.1");
    Response response = new Response(false, version, "Describe", "Error");
    assertEquals(false, response.isSuccess());
    assertEquals(version, response.getVersion());
    assertEquals("Describe", response.getDescription());
    assertEquals("Error", response.getError());
    response.setSuccess(true);
    Version otherVersion = new Version("2.0", "0.2");
    response.setVersion(otherVersion);
    response.setDescription("other Describe");
    response.setError("other Error");
    assertEquals(true, response.isSuccess());
    assertEquals(otherVersion, response.getVersion());
    assertEquals("other Describe", response.getDescription());
    assertEquals("other Error", response.getError());
  }
}
