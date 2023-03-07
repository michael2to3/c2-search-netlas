package c2.search.netlas.target.metasploit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import c2.search.netlas.scheme.DetectResponse;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.Target;

public class Metasploit implements Target {
  private final static Logger LOGGER = LoggerFactory.getLogger(Metasploit.class);

  @Override
  public DetectResponse run(final Host host) {
    DetectResponse response = new DetectResponse();

    response.setName("Metasploit");
    response.setMinVersion("6.3.2");
    response.setMaxVersion("6.3.2");
    response.setCountAllTest(1);

    int count = 0;
    count += checkDefaultBodyResponse(host) ? 1 : 0;

    response.setCountSuccessTest(count);
    return response;
  }

  private boolean checkDefaultBodyResponse(final Host host) {
    String body = "";
    try {
      body = getBodyResponse(host);
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
      return false;
    }
    String defaultBody = "<html><body><h1>It works!</h1></body></html>";
    return body.equals(defaultBody);
  }

  private String getBodyResponse(final Host host) throws IOException {
    URL url = new URL("http://" + host.getHost() + ":" + host.getPort() + "/");
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuilder content = new StringBuilder();

    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }

    in.close();
    con.disconnect();

    return content.toString();
  }
}
