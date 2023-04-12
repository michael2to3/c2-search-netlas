package c2.search.netlas.target.villain;

import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Utils {
  private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

  private Utils() {}

  public static boolean verifyCertFieldsSubject(
      final NetlasWrapper netlasWrapper, final String[] subjectFields)
      throws JsonMappingException, JsonProcessingException {
    final List<List<String>> subject =
        Arrays.asList(
            netlasWrapper.getCertSubjectCountry(),
            netlasWrapper.getCertSubjectProvince(),
            netlasWrapper.getCertSubjectLocality(),
            netlasWrapper.getCertSubjectOrganization(),
            netlasWrapper.getCertSubjectOrganizationUnit(),
            netlasWrapper.getCertSubjectCommonName());
    return allFieldsEqual(subject, subjectFields);
  }

  public static boolean verifyCertFieldsIssuer(
      final NetlasWrapper netlasWrapper, final String[] issuerFields)
      throws JsonMappingException, JsonProcessingException {
    final List<List<String>> issuer =
        Arrays.asList(
            netlasWrapper.getCertIssuerCountry(),
            netlasWrapper.getCertIssuerProvince(),
            netlasWrapper.getCertIssuerLocality(),
            netlasWrapper.getCertIssuerOrganization(),
            netlasWrapper.getCertIssuerOrganizationUnit(),
            netlasWrapper.getCertIssuerCommonName());
    return allFieldsEqual(issuer, issuerFields);
  }

  private static boolean allFieldsEqual(final List<List<String>> fields, final String[] values) {
    for (int i = 0; i < values.length; i++) {
      final String value = values[i];
      if (!value.isEmpty() && !fields.get(i).contains(value)) {
        return false;
      }
    }
    return true;
  }

  public static String getSocketResponse(final Host host) throws IOException {
    try (Socket socket = new Socket(host.getTarget(), host.getPort())) {
      final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      final StringBuilder response = new StringBuilder();
      String line;
      while (true) {
        line = in.readLine();
        if (line == null) {
          break;
        }
        response.append(line);
      }
      return response.toString();
    }
  }

  public static String sendPostRequest(final Host host, final String path)
      throws IOException, NoSuchAlgorithmException, KeyManagementException {
    String url = "https://" + host.getTarget() + ":" + host.getPort() + path;
    final OkHttpClient client = getUnsafeOkHttpClient();
    String responseString = null;
    try {
      final Request request = new Request.Builder().url(url).build();
      final Response response = client.newCall(request).execute();
      responseString = response.body().string();
    } catch (final IOException e) {
      url = "http://" + host.getTarget() + ":" + host.getPort() + path;
      try {
        final Request request = new Request.Builder().url(url).build();
        final Response response = client.newCall(request).execute();
        responseString = response.body().string();
      } catch (final IOException ex) {
        if (LOGGER.isWarnEnabled()) {
          LOGGER.warn("Failed to connect to " + url, ex);
        }
      }
    }
    return responseString;
  }

  private static OkHttpClient getUnsafeOkHttpClient() {
    OkHttpClient okHttpClient = null;
    try {
      final TrustManager[] trustAllCerts =
          new TrustManager[] {
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
            }
          };

      final SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
      final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

      final OkHttpClient.Builder builder = new OkHttpClient.Builder();
      builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
      builder.hostnameVerifier(
          new HostnameVerifier() {
            @Override
            public boolean verify(final String hostname, final SSLSession session) {
              return true;
            }
          });

      okHttpClient = builder.build();
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Failed to create OkHttpClient", e);
      }
    }
    return okHttpClient;
  }
}
