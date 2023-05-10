package c2.search.netlas.target.havoc;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@Detect(name = "Havoc")
public class Havoc {
  @Wire private Host host;

  public Havoc() {}

  @Test(extern = true)
  public boolean checkSendHttpOverHttps() throws IOException {
    final URL url = new URL("http://" + host.getTarget() + ":" + host.getPort() + "/");
    final URLConnection connection = url.openConnection();
    final HttpURLConnection http = (HttpURLConnection) connection;
    http.setRequestMethod("POST");
    http.setDoOutput(true);

    String bodyResponse;
    try (BufferedReader response =
        new BufferedReader(new InputStreamReader(http.getInputStream()))) {
      final StringBuilder sbline = new StringBuilder();
      String line;

      while (true) {
        line = response.readLine();
        if (line == null) {
          break;
        }
        sbline.append(line);
        sbline.append(System.lineSeparator());
      }
      bodyResponse = sbline.toString();
    } catch (final IOException e) {
      bodyResponse = "";
    }
    final int status = 400;
    final String body = "Client sent an HTTP request to an HTTPS server.";
    return bodyResponse.contains(body) && http.getResponseCode() == status;
  }
}
