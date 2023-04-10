package c2.search.netlas.target.deimos;

import c2.search.netlas.scheme.Host;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Utils {
  private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

  private Utils() {}

  public static String[] makeHttpRequest(final Host host, final String path) throws IOException {
    URL url;
    HttpURLConnection connection = null;
    BufferedReader reader = null;
    boolean useHttps = true;
    final List<String> results = new ArrayList<>();

    try {
      url = new URL("https://" + host.getTarget() + ":" + host.getPort() + path);
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setConnectTimeout(5000);
      connection.setReadTimeout(5000);
      connection.connect();

      final int statusCode = connection.getResponseCode();
      results.add(String.valueOf(statusCode));
      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      while (true) {
        line = reader.readLine();
        if (line == null) {
          break;
        }
        results.add(line);
      }
    } catch (final IOException e) {
      useHttps = false;
    } finally {
      if (reader != null) {
        reader.close();
      }
      if (connection != null) {
        connection.disconnect();
      }
    }

    if (!useHttps) {
      try {
        url = new URL("http://" + host.getTarget() + ":" + host.getPort() + path);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();

        final int statusCode = connection.getResponseCode();
        results.add(String.valueOf(statusCode));
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while (true) {
          line = reader.readLine();
          if (line == null) {
            break;
          }
          results.add(line);
        }
      } catch (final IOException e) {
        if (LOGGER.isWarnEnabled()) {
          LOGGER.warn("Could not connect to " + host.getTarget() + ":" + host.getPort() + path, e);
        }
      } finally {
        if (reader != null) {
          reader.close();
        }
        if (connection != null) {
          connection.disconnect();
        }
      }
    }

    return results.toArray(new String[0]);
  }
}
