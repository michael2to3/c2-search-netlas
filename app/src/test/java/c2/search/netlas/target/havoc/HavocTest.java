package c2.search.netlas.target.havoc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.target.NetlasWrapper;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HavocTest {
  @Mock private Host host;
  @Mock private NetlasWrapper netlasWrapper;
  @InjectMocks private Havoc havoc;

  @BeforeEach
  public void setUp() {
    havoc.host = host;
    havoc.netlasWrapper = netlasWrapper;
  }

  @Test
  @DisplayName("Test checkJarm() with JARM")
  public void testCheckJarm() throws IOException {
    String jarm = "3fd21b20d00000021c43d21b21b43de0a012c76cf078b8d06f4620c2286f5e";
    when(netlasWrapper.getJarm()).thenReturn(jarm);
    Response response = havoc.checkJarm();
    assertNotNull(response);
    assertTrue(response.isSuccess());
  }

  @Test
  public void testCheckDefaultBodyResponse() throws Exception {
    when(netlasWrapper.getBody()).thenReturn("404 page not found");
    when(netlasWrapper.getStatusCode()).thenReturn(404);
    when(netlasWrapper.getServers()).thenReturn(new ArrayList<>());
    Response response = havoc.checkDefaultBodyResponse();
    assertNotNull(response);
    assertTrue(response.isSuccess());

    when(netlasWrapper.getServers()).thenReturn(null);
    response = havoc.checkDefaultBodyResponse();

    assertNotNull(response);
    assertTrue(response.isSuccess());
  }

  @Test
  public void testCheckDumbHeader() throws Exception {
    Response response = havoc.checkDumbHeader();
    assertFalse(response.isSuccess());
  }

  @Test
  public void testCheckSendHttpOverHttps() throws Exception {
    Response response = havoc.checkSendHttpOverHttps();
    assertFalse(response.isSuccess());
  }
}
