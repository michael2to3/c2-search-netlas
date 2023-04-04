package c2.search.netlas.target.havoc;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@Detect(name = "Havoc")
public class Havoc {
  @Wire private Host host;
  @Wire private NetlasWrapper netlasWrapper;

  public void setHost(final Host host) {
    this.host = host;
  }

  public void setNetlasWrapper(final NetlasWrapper netlasWrapper) {
    this.netlasWrapper = netlasWrapper;
  }

  @Test
  public Response checkJarm() throws JsonMappingException, JsonProcessingException {
    final String jarm = "3fd21b20d00000021c43d21b21b43de0a012c76cf078b8d06f4620c2286f5e";
    final String rjarm = netlasWrapper.getJarm();
    return new Response(rjarm.equals(jarm));
  }

  @Test
  public Response checkDefaultBodyResponse() throws JsonMappingException, JsonProcessingException {
    final String body = "404 page not found";
    final String rbody = netlasWrapper.getBody();
    final int statusCode = 404;
    final int rstatusCode = netlasWrapper.getStatusCode();

    boolean hasServerHeader = true;
    final List<String> servers = netlasWrapper.getServers();
    hasServerHeader = servers != null && !servers.isEmpty();

    return new Response(rbody.contains(body) && rstatusCode == statusCode && !hasServerHeader);
  }

  @Test(extern = true)
  public Response checkDumbHeader() throws IOException {
    final URL url = new URL("https://" + host + "/");
    final URLConnection connection = url.openConnection();
    final HttpURLConnection http = (HttpURLConnection) connection;
    http.setRequestMethod("POST");
    http.setDoOutput(true);
    final String header = http.getHeaderField("x-ishavocframework");
    return new Response(header != null);
  }

  @Test(extern = true)
  public Response checkSendHttpOverHttps() throws IOException {
    final URL url = new URL("http://" + host.getTarget() + ":" + host.getPort() + "/");
    final URLConnection connection = url.openConnection();
    final HttpURLConnection http = (HttpURLConnection) connection;
    http.setRequestMethod("POST");
    http.setDoOutput(true);

    String bodyResponse;
    try (BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()))) {
      final StringBuilder response = new StringBuilder();
      String line;

      while (true) {
        line = in.readLine();
        if (line == null) {
          break;
        }
        response.append(line);
        response.append(System.lineSeparator());
      }
      bodyResponse = response.toString();
    } catch (final IOException e) {
      bodyResponse = "";
    }
    final int status = 400;
    final String body = "Client sent an HTTP request to an HTTPS server.";
    return new Response(bodyResponse.contains(body) && http.getResponseCode() == status);
  }
}
