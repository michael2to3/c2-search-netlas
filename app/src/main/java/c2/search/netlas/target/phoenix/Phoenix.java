package c2.search.netlas.target.phoenix;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Detect(name = "Phoenix")
public class Phoenix {
  @Wire private Host host;
  @Wire private NetlasWrapper netlasWrapper;

  public Phoenix() {}

  @Test
  public boolean checkJarm() throws JsonMappingException, JsonProcessingException {
    final String jarm = "2ad2ad0002ad2ad22c42d42d000000faabb8fd156aa8b4d8a37853e1063261";
    final String tjarm = netlasWrapper.getJarm();
    return jarm.equals(tjarm);
  }
}
