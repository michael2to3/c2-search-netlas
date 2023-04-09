package c2.search.netlas.target.bruteratel;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Detect(name = "Bruteratel")
public class Bruteratel {
  @Wire private Host host;
  @Wire private NetlasWrapper netlasWrapper;

  public Bruteratel() {}

  @Test
  public boolean checkJarm() throws JsonMappingException, JsonProcessingException {
    final String jarm = "3fd21b20d00000021c43d21b21b43de0a012c76cf078b8d06f4620c2286f5e";
    final String rjarm = netlasWrapper.getJarm();
    return rjarm.equals(jarm);
  }
}
