package c2.search.netlas.target.phoenix;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhoenixTest {
  @InjectMocks private Phoenix phoenix;
  @Mock private Host host;
  @Mock private NetlasWrapper netlasWrapper;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    mockStatic(NetworkUtils.class);
  }

  @BeforeEach
  void setUp() {
    host = Host.newBuilder().setTarget("google.com").setPort(443).setPath("/").build();
  }

  @Test
  void testCheckJarm() throws JsonMappingException, JsonProcessingException {
    String testJarm = "2ad2ad0002ad2ad22c42d42d000000faabb8fd156aa8b4d8a37853e1063261";
    when(netlasWrapper.getJarm()).thenReturn(testJarm);

    boolean result = phoenix.checkJarm();
    assertTrue(result);
  }

  @Test
  void testCheckFieldCert() throws JsonMappingException, JsonProcessingException {
    List<String> rsubCountry = Arrays.asList("US", "CA");
    List<String> rissCountry = Arrays.asList("US", "GB");

    when(netlasWrapper.getCertSubjectCountry()).thenReturn(rsubCountry);
    when(netlasWrapper.getCertIssuerCountry()).thenReturn(rissCountry);

    boolean result = phoenix.checkFieldCert();
    assertTrue(result);
  }

  @Test
  void testCheckDefaultBodyResponse() throws JsonMappingException, JsonProcessingException {
    List<String> body =
        Arrays.asList(
            "bdd3acd38b235f3e79f97834c1bada34fe87489f5cc3c530dab5bc47404e0a87",
            "e9639e3c4681ce85f852fbac48e2eeee5ba51296dbfec57c200d59b76237ab80");

    when(netlasWrapper.getBodyAsSha256()).thenReturn(body.get(0));

    boolean result = phoenix.checkDefaultBodyResponse();
    assertTrue(result);
  }

  @Test
  void testCheckListenerResponse()
      throws KeyManagementException, NoSuchAlgorithmException, IOException {
    int accessDenied = 405;
    when(NetworkUtils.getStatus(any(Host.class), anyString())).thenReturn(accessDenied);
    boolean result = phoenix.checkListenerResponse();
    assertTrue(result);
  }

  @Test
  void testHeaderServer() throws JsonMappingException, JsonProcessingException {
    List<String> servers = Arrays.asList("Werkzeug/2.0.1", "Apache/2.4");
    List<String> types = Arrays.asList("text/html; charset=utf-8", "text/plain");

    when(netlasWrapper.getServers()).thenReturn(servers);
    when(netlasWrapper.getContentType()).thenReturn(types);

    boolean result = phoenix.headerServer();
    assertTrue(result);
  }
}
