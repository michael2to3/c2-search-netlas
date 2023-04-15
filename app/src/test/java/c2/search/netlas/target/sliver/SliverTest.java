package c2.search.netlas.target.sliver;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class SliverTest {
  @InjectMocks private Sliver sliver;
  @Mock private Host host;
  @Mock private NetlasWrapper netlasWrapper;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    mockStatic(Utils.class);
  }

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCheckJarm() throws Exception {
    String jarm = "3fd21c00000000021c43d21c21c43d3795b2a696610c3ae44909dcdcb797f2";
    when(netlasWrapper.getJarm()).thenReturn(jarm);

    assertTrue(sliver.checkJarm());
  }

  @Test
  void testVerifyCertFieldsTeamserver() throws Exception {
    String[] subjectFields = {
      "CA",
      "Newfoundland and Labrador",
      "Mount Pearl",
      "College",
      "distant lettuce, incorporated",
      "localhost",
    };

    when(Utils.verifyCertFieldsSubject(any(), any())).thenReturn(true);

    assertTrue(sliver.verifyCertFieldsTeamserver());
  }

  @Test
  void testCheckRedirect() throws Exception {
    when(Utils.testEndpoint(anyString(), anyInt(), anyInt())).thenReturn(true);

    assertTrue(sliver.checkRedirect());
  }
}
