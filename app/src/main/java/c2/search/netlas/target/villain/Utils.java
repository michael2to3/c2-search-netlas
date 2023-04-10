package c2.search.netlas.target.villain;

import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

final class Utils {
  private Utils() {}

  public static boolean verifyCertFieldsSubject(
      final NetlasWrapper netlasWrapper, final String[] subjectFields)
      throws JsonMappingException, JsonProcessingException {
    final List<String> subCountry = netlasWrapper.getCertSubjectCountry();
    final List<String> subState = netlasWrapper.getCertSubjectProvince();
    final List<String> subCity = netlasWrapper.getCertSubjectLocality();
    final List<String> subOrg = netlasWrapper.getCertSubjectOrganization();
    final List<String> subOrgUnit = netlasWrapper.getCertSubjectOrganizationUnit();
    final List<String> subCommonName = netlasWrapper.getCertSubjectCommonName();

    final List<List<String>> subject =
        Arrays.asList(subCountry, subState, subCity, subOrg, subOrgUnit, subCommonName);

    boolean result = true;
    for (int i = 0; i < subjectFields.length; i++) {
      if (!subjectFields[i].isEmpty() && !allEqual(subject.get(i), subjectFields[i])) {
        result = false;
        break;
      }
    }

    return result;
  }

  public static boolean verifyCertFieldsIssuer(
      final NetlasWrapper netlasWrapper, final String[] issuerFields)
      throws JsonMappingException, JsonProcessingException {
    final List<String> issCountry = netlasWrapper.getCertIssuerCountry();
    final List<String> issState = netlasWrapper.getCertIssuerProvince();
    final List<String> issCity = netlasWrapper.getCertIssuerLocality();
    final List<String> issOrg = netlasWrapper.getCertIssuerOrganization();
    final List<String> issOrgUnit = netlasWrapper.getCertIssuerOrganizationUnit();
    final List<String> issCommonName = netlasWrapper.getCertIssuerCommonName();

    final List<List<String>> issuer =
        Arrays.asList(issCountry, issState, issCity, issOrg, issOrgUnit, issCommonName);

    boolean result = true;
    for (int i = 0; i < issuerFields.length; i++) {
      if (!issuerFields[i].isEmpty() && !allEqual(issuer.get(i), issuerFields[i])) {
        result = false;
        break;
      }
    }

    return result;
  }

  public static boolean allEqual(final List<String> list, final String value) {
    return list.stream().allMatch(s -> s.equals(value));
  }

  public static String getSocketResponse(final Host host) throws IOException {
    String target = host.getTarget();
    int port = host.getPort();

    try (Socket socket = new Socket(target, port)) {
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      StringBuilder response = new StringBuilder();
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
    String urlStr = "https://" + host.getTarget() + ":" + host.getPort() + path;
    URL url = new URL(urlStr);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    
    if (conn instanceof HttpsURLConnection) {
        HttpsURLConnection httpsConn = (HttpsURLConnection) conn;

        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() { return null; }
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, null);
        httpsConn.setSSLSocketFactory(sslContext.getSocketFactory());
        httpsConn.setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) { return true; }
        });
    } else {
        url = new URL("http://" + host.getTarget() + ":" + host.getPort() + path);
        conn = (HttpURLConnection) url.openConnection();
    }

    
    conn.setRequestMethod("POST");
    conn.setDoOutput(true);

    try (Writer writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8")) {
        writer.write(path);
        writer.flush();
    }

    
    StringBuilder response = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
    }

    
    return response.toString();
  }
}
