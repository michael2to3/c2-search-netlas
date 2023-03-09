package c2.search.netlas.target;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import c2.search.netlas.scheme.Host;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NetlasWrapperTest {
  private static final Logger  LOGGER = LoggerFactory.getLogger(NetlasWrapperTest.class);
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

  @Test void testGetResponseBody() throws JsonMappingException, JsonProcessingException {
    String responseBody = netlas.getResponseBody();
    assertNotNull(responseBody);
    assertNotEquals("", responseBody);
    assertTrue(responseBody.length() > 0);
  }
}
