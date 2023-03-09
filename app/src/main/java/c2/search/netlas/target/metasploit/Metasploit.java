package c2.search.netlas.target.metasploit;

import c2.search.netlas.scheme.DetectResponse;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import c2.search.netlas.target.Target;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Metasploit implements Target {
  private static final Logger LOGGER = LoggerFactory.getLogger(Metasploit.class);
  private static final String name = "Metasploit";
  private String maxVersion = "6.x.x";
  private String minVersion = "x.x.x";
  private Host host;
  private NetlasWrapper netlas;

  @Override
  public void setNetlas(final NetlasWrapper netlas) {
    this.netlas = netlas;
  }

  @Override
  public DetectResponse run() {
    DetectResponse response = new DetectResponse();

    response.setName(name);
    response.setMaxVersion(maxVersion);
    response.setMinVersion(minVersion);
    response.setCountAllTest(2);

    int count = 0;
    count += checkDefaultBodyResponse() ? 1 : 0;
    count += checkJarm() ? 1 : 0;

    response.setCountSuccessTest(count);
    return response;
  }

  private boolean checkDefaultBodyResponse() {
    String body = "";
    try {
      body = netlas.getResponseBody();
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
      return false;
    }
    String defaultBody = "<html><body><h1>It works!</h1></body></html>";
    return body.equals(defaultBody);
  }

  boolean checkJarm() {
    String jarmv5 = "07d14d16d21d21d07c42d43d000000f50d155305214cf247147c43c0f1a823";
    String jarmv6 = "07d19d12d21d21d07c42d43d000000f50d155305214cf247147c43c0f1a823";

    String responseJarm = "";
    try {
      responseJarm = netlas.getJarm();
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
    changeMinimalVersion(minVersion);
    return detect;
  }

  private void changeMinimalVersion(String version) {
    if (version.compareTo(minVersion) < 0) {
      minVersion = version;
    }
  }

  private boolean checkUA() {
    String defaultUA =
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 13_1) AppleWebKit/605.1.15 (KHTML, like Gecko)"
            + " Version/16.1 Safari/605.1.15";

    return false;
  }
}
