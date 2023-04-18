package c2.search.netlas.target.phoenix;

import c2.search.netlas.scheme.Host;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

final class NetworkUtils {
  private static final TrustManager[] TRUST_ALL_CERT = {
    new X509TrustManager() {
      @Override
      public void checkClientTrusted(final X509Certificate[] chain, final String authType)
          throws CertificateException {}

      @Override
      public void checkServerTrusted(final X509Certificate[] chain, final String authType)
          throws CertificateException {}

      @Override
      public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
      }
    },
  };

  private NetworkUtils() {}

  private static OkHttpClient getUnsafeOkHttpClient()
      throws NoSuchAlgorithmException, KeyManagementException {
    SSLContext sslContext;
    sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, TRUST_ALL_CERT, new java.security.SecureRandom());
    final HostnameVerifier hostnameVerifier =
        new HostnameVerifier() {
          @Override
          public boolean verify(final String hostname, final SSLSession session) {
            return true;
          }
        };
    final OkHttpClient.Builder builder =
        new OkHttpClient.Builder()
            .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) TRUST_ALL_CERT[0])
            .hostnameVerifier(hostnameVerifier);

    return builder.build();
  }

  public static int getStatus(final Host host, final String path)
      throws KeyManagementException, NoSuchAlgorithmException {
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
      } catch (final IOException e) {
        statusCode = -1;
      }
    }

    return statusCode;
  }
}
