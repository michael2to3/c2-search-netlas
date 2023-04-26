package c2.search.netlas.target.merlin;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import c2.search.netlas.annotation.Test;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;


@ExtendWith(MockitoExtension.class)
public class MerlinTest {
    @InjectMocks private Merlin merlin;
    @Mock private Host host;
    @Mock private NetlasWrapper netlasWrapper;

    @BeforeEach
    public void setUp() {
        merlin.setHost(host);
        merlin.setNetlasWrapper(netlasWrapper);
    }

    @Test
    @DisplayName("Test checkJarm() with JARM")
    public void testCheckJarm() throws IOException {
        final String jarm = "3fd21b20d00000021c43d21b21b43de0a012c76cf078b8d06f4620c2286f5e";
        when(netlasWrapper.getJarm()).thenReturn(jarm);
        boolean response = merlin.checkJarm();
        assertNotNull(response);
        assertTrue(response);
    }

    @Test
    public void testCheckCertSubj() throws Exception {
        final String subject = "";
        when(netlasWrapper.getCertSubjectCountry()).thenReturn(Collections.singletonList(subject));
        boolean response = merlin.checkCertSubj();
        assertNotNull(response);
        assertTrue(response);
    }

    @Test
    public void testCheckHeaders() throws Exception {
        final int contentLen = 0;
        final String contentType = "text/html";
        when(netlasWrapper.getServers()).thenReturn(Collections.emptyList());
        when(netlasWrapper.getContentLength()).thenReturn(Collections.singletonList(contentLen));
        when(netlasWrapper.getContentType()).thenReturn(Collections.singletonList(contentType));
        boolean response = merlin.checkHeaders();
        assertNotNull(response);
        assertTrue(response);
    }
    @Test
    public void testCheckDefaultBodyResponse() throws Exception {
        when(netlasWrapper.getBody()).thenReturn("404");
        when(netlasWrapper.getStatusCode()).thenReturn(404);
        boolean response = merlin.checkBodyResp();
        assertNotNull(response);
        assertTrue(response);
    }
}
