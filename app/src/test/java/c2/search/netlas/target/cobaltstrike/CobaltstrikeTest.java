package c2.search.netlas.target.cobaltstrike;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CobaltStrikeTest {
  @Mock private Host host;
  @Mock private NetlasWrapper netlasWrapper;
  @InjectMocks private CobaltStrike cobaltStrike;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    mockStatic(Utils.class);
  }

  @BeforeEach
  void setUp() throws JsonMappingException, JsonProcessingException {
    MockitoAnnotations.openMocks(this);
    when(host.getTarget()).thenReturn("example.com");
    when(host.getPort()).thenReturn(41337);
  }

  @Test
  void testJarm() throws Exception {
    final String jarm = "2ad2ad16d2ad2ad00042d42d00042ddb04deffa1705e2edc44cae1ed24a4da";
    when(netlasWrapper.getJarm()).thenReturn(jarm);

    assertTrue(cobaltStrike.testJarm());
  }

  @Test
  void defaultCertFieldTeamServer() throws Exception {
    when(Utils.verifyDefaultCertFields(any(), any())).thenReturn(true);
    assertTrue(cobaltStrike.defaultCertFieldTeamServer());
  }

  @Test
  void defaultHeaders() throws Exception {
    final int contentLen = 0;
    final String contentType = "text/plain";

    when(netlasWrapper.getServers()).thenReturn(Collections.emptyList());
    when(netlasWrapper.getContentLength()).thenReturn(Collections.singletonList(contentLen));
    when(netlasWrapper.getContentType()).thenReturn(Collections.singletonList(contentType));

    assertTrue(cobaltStrike.defaultHeaders());
  }

  @Test
  void defaultPort() throws JsonMappingException, JsonProcessingException {
    assertTrue(cobaltStrike.defaultPort());
  }

  @Test
  void lenIdBindShell() throws IOException {
    int dataLength = 120;
    when(Utils.getDataLength(host)).thenReturn(dataLength);

    assertTrue(cobaltStrike.lenIdBindShell());
  }

  @Test
  void checkUA() throws IOException {
    final int accept = 200;
    final int reject = 404;

    when(Utils.sendHttpRequest(any(Host.class), anyString(), anyString())).thenReturn(accept);
    when(Utils.sendHttpRequest(any(Host.class), eq(null), anyString())).thenReturn(reject);

    assertTrue(cobaltStrike.checkUA());
  }
}
