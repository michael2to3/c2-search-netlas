package c2.search.netlas.target;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import c2.search.netlas.cli.Config;
import c2.search.netlas.scheme.Host;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NetlasWrapperTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(NetlasWrapperTest.class);
  private static final String API = new Config("config.properties").get("api.key");
  private static final Host HOST = new Host("ya.ru", 443);
  private static NetlasWrapper netlas;

  @BeforeAll
  static void setup() throws JsonMappingException, JsonProcessingException {
    if(LOGGER.isDebugEnabled()) {
      LOGGER.debug("Use API key: {}", API);
    }
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
  void testGetBody() throws JsonMappingException, JsonProcessingException {
    String responseBody = netlas.getBody();
    assertNotNull(responseBody);
    assertNotEquals("", responseBody);
    assertTrue(responseBody.length() > 0);
    String hash = sha256(responseBody);
    String rhash = netlas.getBodyAsSha256();
    assertEquals(hash, rhash);
  }

  @Test
  void testGetterAndSetter() throws JsonMappingException, JsonProcessingException {
    var netlas = new NetlasWrapper(API, HOST);
    assertNotNull(netlas.getHost());
    assertNotNull(netlas.getNetlas());
  }

  @Test
  void testHeaders() throws JsonMappingException, JsonProcessingException {
    var nn = new NetlasWrapper(API, new Host("vk.com", 443));
    var headers = nn.getHeaders();
    assertNotNull(headers);
    var server = nn.getServers();
    assertNotNull(server);
    assertNotEquals("", server.get(0));

    var status = nn.getStatusCode();
    assertTrue(status >= 200 && status < 600);
  }

  @Test
  void testNotExistKey() throws JsonMappingException, JsonProcessingException {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          netlas.getLast("notExistKey", 0);
        });
  }
}
