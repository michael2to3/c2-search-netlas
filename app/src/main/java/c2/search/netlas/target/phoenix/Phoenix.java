package c2.search.netlas.target.phoenix;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

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

  @Test
  public boolean checkFieldCert() throws JsonMappingException, JsonProcessingException {
    final String subCountry = "US";
    final String issCountry = "US";
    final List<String> rsubCountry = netlasWrapper.getCertSubjectCountry();
    final List<String> rissCountry = netlasWrapper.getCertIssuerCountry();
    return rsubCountry.contains(subCountry) && rissCountry.contains(issCountry);
  }

  @Test
  public boolean checkDefaultBodyResponse() throws JsonMappingException, JsonProcessingException {
    final List<String> body =
        Arrays.asList(
            "bdd3acd38b235f3e79f97834c1bada34fe87489f5cc3c530dab5bc47404e0a87",
            "e9639e3c4681ce85f852fbac48e2eeee5ba51296dbfec57c200d59b76237ab80");
    final String rbody = netlasWrapper.getBodyAsSha256();
    return body.contains(rbody);
  }

  @Test
  public boolean checkListenerResponse()
      throws KeyManagementException, NoSuchAlgorithmException, IOException {
    String[] paths = {"download/history.csv", "download/users.csv", "static/dump.sql"};
    final int accessDenied = 405;
    boolean result = true;
    for (String path : paths) {
      result = NetworkUtils.getStatus(host, path) == accessDenied;
      if (result == false) {
        break;
      }
    }
    return result;
  }

  @Test
  public boolean headerServer() throws JsonMappingException, JsonProcessingException {
    final List<String> servers = netlasWrapper.getServers();
    final List<String> types = netlasWrapper.getContentType();
    final String baseType = "text/html; charset=utf-8";
    final String base = "Werkzeug";
    boolean result = false;
    for (final String server : servers) {
      if (server.contains(base)) {
        result = true;
      }
    }
    return result && types.contains(baseType);
  }
}
