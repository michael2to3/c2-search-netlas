package c2.search.netlas.target.sliver;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Detect(name = "Sliver")
public class Sliver {
  @Wire private Host host;
  @Wire private NetlasWrapper netlasWrapper;

  public Sliver() {}

  @Test
  public boolean checkJarm() throws JsonMappingException, JsonProcessingException {
    final String jarm = "3fd21c00000000021c43d21c21c43d3795b2a696610c3ae44909dcdcb797f2";
    final String tjarm = netlasWrapper.getJarm();
    return jarm.equals(tjarm);
  }

  @Test
  public boolean verifyCertFieldsTeamserver() throws JsonMappingException, JsonProcessingException {
    final String[] subjectFields = {
      "CA",
      "Newfoundland and Labrador",
      "Mount Pearl",
      "College",
      "distant lettuce, incorporated",
      "localhost",
    };
    return Utils.verifyCertFieldsSubject(netlasWrapper, subjectFields);
  }

  @Test
  public boolean verifyCertFieldsListener() throws JsonMappingException, JsonProcessingException {
    final String subComName = "multiplayer";
    final String issuerComName = "operators";
    final List<String> rsubCompName = netlasWrapper.getCertSubjectCommonName();
    final List<String> rissCompName = netlasWrapper.getCertSubjectCommonName();
    return rsubCompName.contains(subComName) && rissCompName.contains(issuerComName);
  }

  @Test
  public boolean checkRedirect() throws NoSuchAlgorithmException {
    final int redirect = 301;
    final int len = 0;
    return Utils.testEndpoint(host + "//", redirect, len);
  }
}
