package c2.search.netlas.target.sliver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Utils {
  private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

  private Utils() {}

  public static boolean compareList(final List<List<String>> lhs, final List<String> rhs) {
    boolean result = true;
    for (int i = 0; i < lhs.size(); i++) {
      if (!lhs.get(i).contains(rhs.get(i))) {
        result = false;
        break;
      }
    }
    return result;
  }

  public static int[] getHttpResponse(final String path) {
    int responseCode = -1;
    int contentLength = -1;
    try {
      final URL url = new URL(path);
      final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      responseCode = connection.getResponseCode();
      contentLength = connection.getContentLength();

      connection.disconnect();
    } catch (IOException e) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Failed to get response from {}", path, e);
      }
    }

    return new int[] {responseCode, contentLength};
  }

  public static boolean testEndpoint(final String endpoint, final int expStatus, final int expLen)
      throws NoSuchAlgorithmException {
    final int[] http = Utils.getHttpResponse(String.format("http://%s", endpoint));
    final int[] https = Utils.getHttpResponse(String.format("https://%s", endpoint));
    final boolean eqStatus = http[0] == expStatus || https[0] == expStatus;
    final boolean eqLen = http[1] == expLen || https[1] == expLen;
    return eqStatus && eqLen;
  }
}
