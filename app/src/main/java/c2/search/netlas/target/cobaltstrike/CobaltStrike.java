package c2.search.netlas.target.cobaltstrike;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;

@Detect(name = "CobaltStrike")
public class CobaltStrike {
  @Wire private Host host;
  @Wire private NetlasWrapper netlasWrapper;

  @Test
  public boolean testJarm() {
    final String jarm = "2ad2ad16d2ad2ad00042d42d00042ddb04deffa1705e2edc44cae1ed24a4da";
    String tjarm;
    try {
      tjarm = netlasWrapper.getJarm();
    } catch (JsonProcessingException e) {
      tjarm = null;
    }

    return jarm.equals(tjarm);
  }
}
