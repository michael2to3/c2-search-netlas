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
  public boolean testJarm() throws JsonMappingException, JsonProcessingException {
    final String jarm = "2ad2ad16d2ad2ad00042d42d00042ddb04deffa1705e2edc44cae1ed24a4da";
    final String tjarm = netlasWrapper.getJarm();

    return jarm.equals(tjarm);
  }

  @Test
  public boolean defaultCertFieldTeamServer() throws JsonMappingException, JsonProcessingException {
    final String[] fields = {
      "US",
      "Washington",
      "Redmond",
      "Microsoft Corporation",
      "Microsoft Corporation",
      "Outlook.live.com",
    };
    return verifyDefaultCertFields(fields);
  }

  @Test
  public boolean defaultCertFieldListener() throws JsonMappingException, JsonProcessingException {
    final String[] fields = {"", "", "", "", "", ""};
    return verifyDefaultCertFields(fields);
  }

  private boolean verifyDefaultCertFields(final String[] fields)
      throws JsonMappingException, JsonProcessingException {
    final List<String> subCountry = netlasWrapper.getCertSubjectCountry();
    final List<String> subState = netlasWrapper.getCertSubjectProvince();
    final List<String> subCity = netlasWrapper.getCertSubjectLocality();
    final List<String> subOrg = netlasWrapper.getCertSubjectOrganization();
    final List<String> subOrgUnit = netlasWrapper.getCertSubjectOrganizationUnit();
    final List<String> subCommonName = netlasWrapper.getCertSubjectCommonName();

    final List<String> issCountry = netlasWrapper.getCertIssuerCountry();
    final List<String> issState = netlasWrapper.getCertIssuerProvince();
    final List<String> issCity = netlasWrapper.getCertIssuerLocality();
    final List<String> issOrg = netlasWrapper.getCertIssuerOrganization();
    final List<String> issOrgUnit = netlasWrapper.getCertIssuerOrganizationUnit();
    final List<String> issCommonName = netlasWrapper.getCertIssuerCommonName();

    final List<List<String>> subject =
        Arrays.asList(subCountry, subState, subCity, subOrg, subOrgUnit, subCommonName);
    final List<List<String>> issuer =
        Arrays.asList(issCountry, issState, issCity, issOrg, issOrgUnit, issCommonName);

    boolean result = true;
    for (int i = 0; i < fields.length; i++) {
      if (!fields[i].equals("")) {
        if (!allEqual(subject.get(i), fields[i])) {
          result = false;
          break;
        }
        if (!allEqual(issuer.get(i), fields[i])) {
          result = false;
          break;
        }
      }
    }

    return result;
  }

  private boolean allEqual(final List<String> list, final String value) {
    return list.stream().allMatch(s -> s.equals(value));
  }

  @Test
  public boolean defaultHeaders() throws JsonMappingException, JsonProcessingException {
    final int contentLen = 0;
    final String contentType = "text/plain";
    final boolean isEmptyServer = netlasWrapper.getServers().isEmpty();
    final boolean isDefaultLen = netlasWrapper.getContentLength().contains(contentLen);
    final boolean isDefaultType = netlasWrapper.getContentType().contains(contentType);

    return isEmptyServer && isDefaultLen && isDefaultType;
  }

  @Test
  public boolean defaultPort() throws JsonMappingException, JsonProcessingException {
    final List<Integer> ports = Arrays.asList(41337, 1337, 4444, 2222, 50050);
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
    final List<String> paths = Arrays.asList("/accept.php", "/pixel");
    for (final String path : paths) {
      final int statusWithUA = sendHttpRequest("random", path);
      final int statusWithoutUA = sendHttpRequest(null, path);
      final int accept = 200;
      final int reject = 404;
      result = statusWithUA == accept && statusWithoutUA == reject;
    }
    return result;
  }

  private int getDataLength() throws IOException {
    final int chunk = 1024;
    try (Socket socket = new Socket(host.getTarget(), host.getPort());
        InputStream inputStream = socket.getInputStream()) {
      final byte[] buffer = new byte[chunk];
      int len;
      int totalLen = 0;
      while (true) {
        len = inputStream.read(buffer);
        if (len == -1) {
          break;
        }
        totalLen += len;
      }
      return totalLen;
    }
  }

  public int sendHttpRequest(final String ua, final String path) throws IOException {
    final String urlString = "http://" + host.getTarget() + ":" + host.getPort() + path;
    final URL url = new URL(urlString);
    final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    if (!ua.isEmpty() || ua == null) {
      connection.setRequestProperty("User-Agent", "random");
    }
    final int statusCode = connection.getResponseCode();
    connection.disconnect();
    return statusCode;
  }
}
