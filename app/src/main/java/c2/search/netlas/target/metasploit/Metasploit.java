package c2.search.netlas.target.metasploit;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.Version;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Detect(name = "Metasploit")
public class Metasploit {
  private static final Logger LOGGER = LoggerFactory.getLogger(Metasploit.class);
  @Wire private Host host;
  @Wire private NetlasWrapper netlasWrapper;

  @Test
  public Response checkDefaultBodyResponse() {
    String body = "";
    try {
      body = netlasWrapper.getResponseBody();
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
      return new Response(false);
    }
    String defaultBody = "<html><body><h1>It works!</h1></body></html>";
    return new Response(body.equals(defaultBody));
  }

  @Test
  public Response checkJarm() {
    String jarmv5 = "07d14d16d21d21d07c42d43d000000f50d155305214cf247147c43c0f1a823";
    String jarmv6 = "07d19d12d21d21d07c42d43d000000f50d155305214cf247147c43c0f1a823";

    String responseJarm = "";
    try {
      responseJarm = netlasWrapper.getJarm();
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage());
    }

    String minVersion = "6.x.x";
    boolean detect = false;
    if (responseJarm.equals(jarmv5)) {
      minVersion = "5.x.x";
      detect = true;
    }
    if (responseJarm.equals(jarmv6)) {
      minVersion = "6.x.x";
      detect = true;
    }
    return new Response(detect, new Version(null, minVersion));
  }

  private boolean checkUA() {
    String defaultUA =
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 13_1) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/16.1 Safari/605.1.15";

    return false;
  }
}
