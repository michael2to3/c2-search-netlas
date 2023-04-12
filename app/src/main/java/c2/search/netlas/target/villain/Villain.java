package c2.search.netlas.target.villain;

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
import java.util.List;

@Detect(name = "Villain")
public class Villain {
  @Wire private Host host;
  @Wire private NetlasWrapper netlasWrapper;

  public Villain() {}

  @Test
  public boolean checkJarm() throws JsonMappingException, JsonProcessingException {
    final String jarm = "2ad2ad0002ad2ad22c42d42d000000faabb8fd156aa8b4d8a37853e1063261";
    final String rjarm = netlasWrapper.getJarm();
    return jarm.equals(rjarm);
  }

  @Test
  public boolean checkFieldCert() throws JsonMappingException, JsonProcessingException {
    // * subject: C=AU; ST=Some-State; O=Internet Widgits Pty Ltd
    // * issuer: C=AU; ST=Some-State; O=Internet Widgits Pty Ltd
    final String subCoutry = "AU";
    final String subState = "Some-State";
    final String subOrg = "Internet Widgits Pty Ltd";
    final String issCountry = "AU";
    final String issState = "Some-State";
    final String issOrg = "Internet Widgits Pty Ltd";

    final String[] subject = {subCoutry, subState, subOrg};
    final String[] issuer = {issCountry, issState, issOrg};

    return CertificateUtils.verifyCertFieldsSubject(netlasWrapper, subject)
        && CertificateUtils.verifyCertFieldsIssuer(netlasWrapper, issuer);
  }

  @Test
  public boolean headerHttp() throws JsonMappingException, JsonProcessingException {
    final List<String> servers = netlasWrapper.getServers();
    final String base = "BaseHTTP";
    boolean result = false;
    for (final String server : servers) {
      if (server.contains(base)) {
        result = true;
        break;
      }
    }
    return result;
  }

  @Test
  public boolean checkHandlerTcp() throws IOException {
    final String base = "whoami";
    final String resp = Utils.getSocketResponse(host);
    return resp.contains(base);
  }

  @Test
  public boolean checkPostResponse()
      throws IOException, KeyManagementException, NoSuchAlgorithmException {
    final String resp = Utils.sendPostRequest(host, "/");
    final String body = "Move on mate.";
    return resp.contains(body);
  }
}
