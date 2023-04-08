package c2.search.netlas.target.cobaltstrike;

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

class Utils {
  public static int getDataLength(final Host host) throws IOException {
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

  public static boolean allEqual(final List<String> list, final String value) {
    return list.stream().allMatch(s -> s.equals(value));
  }

  public static int sendHttpRequest(final Host host, final String ua, final String path)
      throws IOException {
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

  public static boolean verifyDefaultCertFields(
      final NetlasWrapper netlasWrapper, final String[] fields)
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
}
