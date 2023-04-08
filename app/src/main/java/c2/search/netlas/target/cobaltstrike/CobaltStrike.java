package c2.search.netlas.target.cobaltstrike;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Detect(name = "CobaltStrike")
public class CobaltStrike {
  private static final Logger LOGGER = LoggerFactory.getLogger(CobaltStrike.class);
  @Wire private Host host;
  @Wire private NetlasWrapper netlasWrapper;

  public CobaltStrike() {}

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

  @Test
  public boolean defaultCertFieldTeamServer() throws JsonMappingException, JsonProcessingException {
    // * subject: C=US; ST=Washington; L=Redmond; O=Microsoft Corporation;
    // OU=Microsoft Corporation; CN=Outlook.live.com
    // * issuer: C=US; ST=Washington; L=Redmond; O=Microsoft Corporation;
    // OU=Microsoft Corporation; CN=Outlook.live.com
    final String country = "US";
    final String state = "Washington";
    final String city = "Redmond";
    final String organization = "Microsoft Corporation";
    final String organizationUnit = "Microsoft Corporation";
    final String commonName = "Outlook.live.com";

    final List<String> subjectCountry = netlasWrapper.getCertSubjectCountry();
    final List<String> subjectState = netlasWrapper.getCertSubjectProvince();
    final List<String> subjectCity = netlasWrapper.getCertSubjectLocality();
    final List<String> subjectOrganization = netlasWrapper.getCertSubjectOrganization();
    final List<String> subjectOrganizationUnit = netlasWrapper.getCertSubjectOrganizationUnit();
    final List<String> subjectCommonName = netlasWrapper.getCertSubjectCommonName();

    final List<String> issuerCountry = netlasWrapper.getCertIssuerCountry();
    final List<String> issuerState = netlasWrapper.getCertIssuerProvince();
    final List<String> issuerCity = netlasWrapper.getCertIssuerLocality();
    final List<String> issuerOrganization = netlasWrapper.getCertIssuerOrganization();
    final List<String> issuerOrganizationUnit = netlasWrapper.getCertIssuerOrganizationUnit();
    final List<String> issuerCommonName = netlasWrapper.getCertIssuerCommonName();

    final List<List<String>> subject =
        Arrays.asList(
            subjectCountry,
            subjectState,
            subjectCity,
            subjectOrganization,
            subjectOrganizationUnit,
            subjectCommonName);
    final List<List<String>> issuer =
        Arrays.asList(
            issuerCountry,
            issuerState,
            issuerCity,
            issuerOrganization,
            issuerOrganizationUnit,
            issuerCommonName);

    return allEqual(subject, country, state, city, organization, organizationUnit, commonName)
        && allEqual(issuer, country, state, city, organization, organizationUnit, commonName);
  }

  @Test
  public boolean defaultCertFieldListener() throws JsonMappingException, JsonProcessingException {
    // * subject: C=; ST=; L=; O=; OU=; CN=
    // * issuer: C=; ST=; L=; O=; OU=; CN=
    final String country = "";
    final String state = "";
    final String city = "";
    final String organization = "";
    final String organizationUnit = "";
    final String commonName = "";

    final List<String> subjectCountry = netlasWrapper.getCertSubjectCountry();
    final List<String> subjectState = netlasWrapper.getCertSubjectProvince();
    final List<String> subjectCity = netlasWrapper.getCertSubjectLocality();
    final List<String> subjectOrganization = netlasWrapper.getCertSubjectOrganization();
    final List<String> subjectOrganizationUnit = netlasWrapper.getCertSubjectOrganizationUnit();
    final List<String> subjectCommonName = netlasWrapper.getCertSubjectCommonName();

    final List<String> issuerCountry = netlasWrapper.getCertIssuerCountry();
    final List<String> issuerState = netlasWrapper.getCertIssuerProvince();
    final List<String> issuerCity = netlasWrapper.getCertIssuerLocality();
    final List<String> issuerOrganization = netlasWrapper.getCertIssuerOrganization();
    final List<String> issuerOrganizationUnit = netlasWrapper.getCertIssuerOrganizationUnit();
    final List<String> issuerCommonName = netlasWrapper.getCertIssuerCommonName();

    final List<List<String>> subject =
        Arrays.asList(
            subjectCountry,
            subjectState,
            subjectCity,
            subjectOrganization,
            subjectOrganizationUnit,
            subjectCommonName);
    final List<List<String>> issuer =
        Arrays.asList(
            issuerCountry,
            issuerState,
            issuerCity,
            issuerOrganization,
            issuerOrganizationUnit,
            issuerCommonName);

    return allEqual(subject, country, state, city, organization, organizationUnit, commonName)
        && allEqual(issuer, country, state, city, organization, organizationUnit, commonName);
  }

  @Test
  public boolean defaultHeaders() throws JsonMappingException, JsonProcessingException {
    int defaultContentLen = 0;
    String defaultContentType = "text/plain";
    boolean emptyServer = netlasWrapper.getServers().isEmpty();
    boolean len = netlasWrapper.getContentLength().contains(defaultContentLen);
    boolean contentType = netlasWrapper.getContentType().contains(defaultContentType);

    return emptyServer && len && contentType;
  }

  @Test
  public boolean defaultPort() throws JsonMappingException, JsonProcessingException {
    List<Integer> ports = Arrays.asList(41337, 1337, 4444, 2222, 50050);
    return ports.contains(host.getPort());
  }

  @Test(extern = true)
  public boolean lenIdBindShell() throws JsonMappingException, JsonProcessingException {
    final int min = 100;
    final int max = 150;
    boolean result = false;
    try {
      final int len = getDataLength();
      result = len >= min && len <= max;
    } catch (IOException e) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Failed to get data length", e);
      }
    }
    return result;
  }

  @Test(extern = true)
  public boolean checkUA() throws IOException {
    boolean result = false;
    List<String> paths = Arrays.asList("/accept.php", "/pixel");
    for (String path : paths) {
      int statusWithUA = sendHttpRequest("random", path);
      int statusWithoutUA = sendHttpRequest(null, path);
      int accept = 200;
      int reject = 404;
      result = statusWithUA == accept && statusWithoutUA == reject;
    }
    return result;
  }

  private boolean allEqual(List<List<String>> lists, String... expectedValues) {
    boolean result;
    if (lists.size() != expectedValues.length) {
      result = false;
    } else {
      for (int i = 0; i < lists.size(); i++) {
        if (!lists.get(i).contains(expectedValues[i])) {
          result = false;
          break;
        }
      }
      result = true;
    }
    return result;
  }

  private int getDataLength() throws IOException {
    Socket socket = null;
    InputStream inputStream = null;
    try {
      socket = new Socket(host.getTarget(), host.getPort());
      inputStream = socket.getInputStream();
      byte[] buffer = new byte[1024];
      int len;
      int totalLen = 0;
      while ((len = inputStream.read(buffer)) != -1) {
        totalLen += len;
      }
      return totalLen;
    } catch (IOException e) {
      throw e;
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
      if (socket != null) {
        socket.close();
      }
    }
  }

  public int sendHttpRequest(String ua, String path) throws IOException {
    String urlString = "http://" + host.getTarget() + ":" + host.getPort() + path;
    URL url = new URL(urlString);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    if (!ua.isEmpty() || ua == null) {
      connection.setRequestProperty("User-Agent", "random");
    }
    int statusCode = connection.getResponseCode();
    connection.disconnect();
    return statusCode;
  }
}