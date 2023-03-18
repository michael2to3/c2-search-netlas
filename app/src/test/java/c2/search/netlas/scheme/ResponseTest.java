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

  @Test
  void testConstructor() {
    Response response = new Response();
    response.setSuccess(true);
    Version ver = new Version("2.0", "0.2");
    response.setVersion(ver);
    response.setDescription("other Describe");
    response.setError("other Error");

    assertEquals(true, response.isSuccess());
    assertEquals(ver, response.getVersion());
    assertEquals("other Describe", response.getDescription());
    assertEquals("other Error", response.getError());

    response = new Response(false);
    assertEquals(false, response.isSuccess());

    Version version = new Version("3.0", "0.3");
    response = new Response(true, version);
    assertEquals(true, response.isSuccess());
    assertEquals(version, response.getVersion());

    response = new Response(false, version, "3desc");
    assertEquals(false, response.isSuccess());
    assertEquals(version, response.getVersion());
    assertEquals("3desc", response.getDescription());

    response = new Response(true, ver, "4desc", "4err");
    assertEquals(true, response.isSuccess());
    assertEquals(ver, response.getVersion());
    assertEquals("4desc", response.getDescription());
    assertEquals("4err", response.getError());
  }

  @Test
  void testEquals() {
    Response response = new Response();
    Response other = new Response();
    assertEquals(response.toString(), other.toString());
    assertEquals(response.hashCode(), other.hashCode());
    assertNotEquals(response, null);
    assertNotEquals(response, "");
    assertNotEquals(response, new Object());
  }
}
