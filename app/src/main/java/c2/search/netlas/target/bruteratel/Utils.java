package c2.search.netlas.target.bruteratel;

import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Utils {
  private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
  private static final OkHttpClient CLIENT = getUnsafeOkHttpClient();

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
      if (!allEqual(subject.get(i), subjectFields[i])) {
        result = false;
        break;
      }
    }
    for (int i = 0; i < issuerFields.length; i++) {
      if (!allEqual(issuer.get(i), issuerFields[i])) {
        result = false;
        break;
      }
    }

    return result;
  }

  public static String[] getHttpResponse(final String path) {
    int responseCode = -1;
    String responseBody = "";
    try {
      final Request request = new Request.Builder().url(path).build();
      final Response response = CLIENT.newCall(request).execute();
      responseCode = response.code();
      responseBody = response.body().string();
    } catch (IOException e) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Failed to get response from {}", path, e);
      }
    }

    return new String[] {String.valueOf(responseCode), responseBody};
  }

  public static String getSHA256Hash(final String input) throws NoSuchAlgorithmException {
    final MessageDigest digest = MessageDigest.getInstance("SHA-256");
    final byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
    final StringBuilder hexString = new StringBuilder();
    final int base = 0xff;
    final int length = 1;
    for (final byte b : hash) {
      final String hex = Integer.toHexString(base & b);
      if (hex.length() == length) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

  public static boolean testEndpoint(
      final String endpoint, final int expectedStatus, final String expectedHash)
      throws NoSuchAlgorithmException {
    final String[] http = Utils.getHttpResponse(String.format("http://%s", endpoint));
    final String[] https = Utils.getHttpResponse(String.format("https://%s", endpoint));
    final String rbody = Utils.getSHA256Hash(http[1]);
    final String rbody2 = Utils.getSHA256Hash(https[1]);
    final int rcode = Integer.parseInt(http[0]);
    final int rcode2 = Integer.parseInt(https[0]);
    final boolean eqBody = rbody.equals(expectedHash) || rbody2.equals(expectedHash);
    final boolean eqStatus = rcode == expectedStatus && rcode2 == expectedStatus;
    return eqBody && eqStatus;
  }

  private static OkHttpClient getUnsafeOkHttpClient() {
    OkHttpClient okHttpClient = null;
    try {
      final TrustManager[] trustAllCerts = {
        new X509TrustManager() {
          @Override
          public void checkClientTrusted(
              final java.security.cert.X509Certificate[] chain, final String authType)
              throws CertificateException {}

          @Override
          public void checkServerTrusted(
              final java.security.cert.X509Certificate[] chain, final String authType)
              throws CertificateException {}

          @Override
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
          }
        },
      };
      final SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
      final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
      final OkHttpClient.Builder builder = new OkHttpClient.Builder();
      builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
      builder.hostnameVerifier((hostname, session) -> true);
      okHttpClient = builder.build();
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Failed to create OkHttpClient", e);
      }
    }
    return okHttpClient;
  }
}
