package c2.search.netlas.target.cobaltstrike;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Detect(name = "CobaltStrike")
public class CobaltStrike {
  private static final Logger LOGGER = LoggerFactory.getLogger(CobaltStrike.class);
  @Wire private Host host;

  public CobaltStrike() {}

  @Test(extern = true)
  public boolean lenIdBindShell() throws JsonMappingException, JsonProcessingException {
    final int min = 100;
    final int max = 150;
    boolean result = false;
    try {
      final int len = Utils.getDataLength(host);
      result = len >= min && len <= max;
    } catch (IOException e) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Failed to get data length", e);
      }
    }
    return result;
  }

  @Test(extern = true)
  public boolean checkUA() throws IOException {
    boolean result = false;
    final List<String> paths = Arrays.asList("/accept.php", "/pixel");
    for (final String path : paths) {
      final int statusWithUA = Utils.sendHttpRequest(host, "random", path);
      final int statusWithoutUA = Utils.sendHttpRequest(host, null, path);
      final int accept = 200;
      final int reject = 404;
      result = statusWithUA == accept && statusWithoutUA == reject;
    }
    return result;
  }
}
