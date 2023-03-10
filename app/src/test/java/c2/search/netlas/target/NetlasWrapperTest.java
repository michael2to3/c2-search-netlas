package c2.search.netlas.target;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import c2.search.netlas.scheme.Host;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import netlas.java.Netlas;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NetlasWrapperTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(NetlasWrapperTest.class);
  private static final String API = System.getenv("API_KEY");
  private static final Host HOST = new Host("ya.ru", 443);
  private static NetlasWrapper netlas;

  @BeforeAll
  static void setup() throws JsonMappingException, JsonProcessingException {
    LOGGER.info("Use API key: {}", API);
    netlas = new NetlasWrapper(API, HOST);
  }

  @Test
  void testGetDnsName() throws JsonMappingException, JsonProcessingException {
    List<String> dnss = netlas.getDnsName();
    assertTrue(dnss.size() > 0);
    assertNotNull(dnss.get(0));
    assertNotEquals("", dnss.get(0));
  }

  @Test
  void testGetJarm() throws JsonMappingException, JsonProcessingException {
    String jarm = netlas.getJarm();
    assertNotNull(jarm);
    assertNotEquals("", jarm);
    assertTrue(jarm.length() == 62);
  }

  public static String sha256(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
      StringBuilder hexString = new StringBuilder();

      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }

      return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void testGetResponseBody() throws JsonMappingException, JsonProcessingException {
    String responseBody = netlas.getResponseBody();
    assertNotNull(responseBody);
    assertNotEquals("", responseBody);
    assertTrue(responseBody.length() > 0);
    String hash = sha256(responseBody);
    String hashResponse = netlas.getResponseBodyAsSha256();
    assertEquals(hash, hashResponse);
  }

  @Test
  void testGetterAndSetter() throws JsonMappingException, JsonProcessingException {
    var netlas = new NetlasWrapper(API, HOST);
    assertNotNull(netlas.getHost());
    assertNotNull(netlas.getNetlas());

    netlas.setHost(HOST);
    netlas.setNetlas(new Netlas(API));
    assertNotNull(netlas.getHost());
    assertNotNull(netlas.getNetlas());
  }
}
