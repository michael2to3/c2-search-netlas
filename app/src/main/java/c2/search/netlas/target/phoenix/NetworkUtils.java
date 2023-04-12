package c2.search.netlas.target.phoenix;

import c2.search.netlas.scheme.Host;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

final class NetworkUtils {
  private NetworkUtils() {}

  private static final TrustManager[] TRUST_ALL_CERT = {
    new X509TrustManager() {
      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[] {};
      }

      public void checkClientTrusted(
          final java.security.cert.X509Certificate[] chain, final String authType)
          throws java.security.cert.CertificateException {}

      public void checkServerTrusted(
          final java.security.cert.X509Certificate[] chain, final String authType)
          throws java.security.cert.CertificateException {}
    },
  };

  private static OkHttpClient getUnsafeOkHttpClient()
      throws NoSuchAlgorithmException, KeyManagementException {
    final SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, TRUST_ALL_CERT, new java.security.SecureRandom());
    final OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) TRUST_ALL_CERT[0]);
    builder.hostnameVerifier((hostname, session) -> true);
    return builder.build();
  }

  public static int getStatus(final Host host, final String path)
      throws IOException, KeyManagementException, NoSuchAlgorithmException {
    int statusCode;

    String url = "https://" + host.getTarget() + ":" + host.getPort() + path;
    OkHttpClient client = getUnsafeOkHttpClient();
    Request request = new Request.Builder().url(url).build();
    try (Response response = client.newCall(request).execute()) {
      statusCode = response.code();
    } catch (final IOException ex) {
      url = "http://" + host.getTarget() + ":" + host.getPort() + path;
      client = new OkHttpClient();
      request = new Request.Builder().url(url).build();
      try (Response response = client.newCall(request).execute()) {
        statusCode = response.code();
      } catch (IOException e) {
        throw e;
      }
    }

    return statusCode;
  }
}
