package c2.search.netlas.target.bruteratel;

import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Utils {
  private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

  private Utils() {}

  public static boolean allEqual(final List<String> list, final String value) {
    return list.stream().allMatch(s -> s.equals(value));
  }

  public static boolean verifyCertFields(
      final NetlasWrapper netlasWrapper, final String[] subjectFields, final String[] issuerFields)
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
    for (int i = 0; i < subjectFields.length; i++) {
      if (!subjectFields[i].isEmpty() && !allEqual(subject.get(i), subjectFields[i])) {
        result = false;
        break;
      }
    }
    for (int i = 0; i < issuerFields.length; i++) {
      if (!issuerFields[i].isEmpty() && !allEqual(issuer.get(i), issuerFields[i])) {
        result = false;
        break;
      }
    }

    return result;
  }

  public static String[] getHTTPResponse(final String path) {
    int responseCode = -1;
    String responseBody = "";
    try {
      final URL url = new URL(path);
      final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      responseCode = connection.getResponseCode();
      responseBody = readResponseBody(connection);

      connection.disconnect();
    } catch (IOException e) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Failed to get response from {}", path, e);
      }
    }

    return new String[] {String.valueOf(responseCode), responseBody};
  }

  private static String readResponseBody(final HttpURLConnection connection) throws IOException {
    final BufferedReader in =
        new BufferedReader(new InputStreamReader(connection.getInputStream()));
    final StringBuilder response = new StringBuilder();
    String inputLine;
    while (true) {
      inputLine = in.readLine();
      if (inputLine == null) {
        break;
      }
      response.append(inputLine);
    }
    in.close();
    return response.toString();
  }

  public static String getSHA256Hash(final String input) throws NoSuchAlgorithmException {
    final MessageDigest digest = MessageDigest.getInstance("SHA-256");
    final byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
    final StringBuilder hexString = new StringBuilder();
    for (final byte b : hash) {
      final String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

  public static boolean testEndpoint(
      final String endpoint, final int expectedStatus, final String expectedHash)
      throws NoSuchAlgorithmException {
    final String[] http = Utils.getHTTPResponse(String.format("http://%s", endpoint));
    final String[] https = Utils.getHTTPResponse(String.format("https://%s", endpoint));
    final String rbody = Utils.getSHA256Hash(http[1]);
    final String rbody2 = Utils.getSHA256Hash(https[1]);
    final int rcode = Integer.parseInt(http[0]);
    final int rcode2 = Integer.parseInt(https[0]);
    final boolean eqBody = rbody.equals(expectedHash) || rbody2.equals(expectedHash);
    final boolean eqStatus = rcode == expectedStatus && rcode2 == expectedStatus;
    return eqBody && eqStatus;
  }
}
