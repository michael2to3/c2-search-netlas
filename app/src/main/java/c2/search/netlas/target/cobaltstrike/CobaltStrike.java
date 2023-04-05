package c2.search.netlas.target.cobaltstrike;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.target.NetlasWrapper;

@Detect(name = "CobaltStrike")
public class CobaltStrike {
  @Wire private Host host;
  @Wire private NetlasWrapper netlasWrapper;

  @Test
  public Response testJarm() {
    return new Response(false);
  }
}
