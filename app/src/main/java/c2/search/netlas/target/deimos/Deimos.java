package c2.search.netlas.target.deimos;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Detect(name = "Deimos")
public class Deimos {
  @Wire private Host host;
  @Wire private NetlasWrapper netlasWrapper;

  public Deimos() {}

  @Test
  public boolean checkJarm() throws JsonMappingException, JsonProcessingException {
    final List<String> jarm =
        Arrays.asList(
            "00000000000000000041d00000041d9535d5979f591ae8e547c5e5743e5b64",
            "1bd1bd1bd0001bd00041d1bd1bd41db0fe6e6bbf8c4edda78e3ec2bfb55687");
    final String rjarm = netlasWrapper.getJarm();
    return jarm.contains(rjarm);
  }

  @Test
  public boolean checkCert() throws JsonMappingException, JsonProcessingException {
    final String subIssOrg = "Acme Co";
    final List<String> subOrg = netlasWrapper.getCertSubjectOrganization();
    final List<String> issOrg = netlasWrapper.getCertIssuerOrganization();
    final boolean hasSub = subOrg.contains(subIssOrg);
    final boolean hasIss = issOrg.contains(subIssOrg);
    return hasSub && hasIss;
  }

  @Test
  public boolean checkRedirect() throws IOException {
    final List<String> pathes =
        Arrays.asList(
            "//",
            "/webdav/index.html",
            "/webadmin/index.html",
            "/tiny_mce/plugins/imagemanager/pages/im/index.html",
            "/templates/index.html",
            "/swagger/index.html",
            "/panel-administracion/index.html",
            "/modelsearch/index.html",
            "/mifs/user/index.html",
            "/index.html",
            "/demo/ejb/index.html",
            "/bb-admin/index.html");
    final int redirect = 301;
    boolean result = true;
    for (final String path : pathes) {
      final String[] resp = Utils.makeHttpRequest(host, path);
      final int status = Integer.parseInt(resp[0]);
      final String body = resp[1];
      if (status != redirect || !body.isEmpty()) {
        result = false;
        break;
      }
    }
    return result;
  }

  @Test
  public boolean checkUbuntuDefaultPage() throws JsonMappingException, JsonProcessingException {
    final String sha256 = "601d4cab0d7d8b3985b73f6ac6130f3b2a43a286d1f5350d8320de9ee501777c";
    final String body = netlasWrapper.getBodyAsSha256();
    return sha256.equals(body);
  }

  @Test 
  public boolean loginPage() throws JsonMappingException, JsonProcessingException {
    final String sha256 = "4bb3ee8361794353b8f76f97778fd377838b1354193b552cad5b3bf742cc9b34";
    final String body = netlasWrapper.getBodyAsSha256();
    return body.contains(sha256);
  }
}
