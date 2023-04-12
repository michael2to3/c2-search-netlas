package c2.search.netlas.target.villain;

import c2.search.netlas.scheme.Host;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
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

  public static String sendPostRequest(final Host host, final String path)
      throws IOException, NoSuchAlgorithmException, KeyManagementException {
    String url = "https://" + host.getTarget() + ":" + host.getPort() + path;
    final OkHttpClient client = getUnsafeOkHttpClient();
    String responseString = null;
    try {
      final Request request = new Request.Builder().url(url).build();
      final Response response = client.newCall(request).execute();
      responseString = response.body().string();
    } catch (IOException e) {
      url = "http://" + host.getTarget() + ":" + host.getPort() + path;
      try {
        final Request request = new Request.Builder().url(url).build();
        final Response response = client.newCall(request).execute();
        responseString = response.body().string();
      } catch (IOException ex) {
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
