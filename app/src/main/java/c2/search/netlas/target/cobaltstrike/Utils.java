package c2.search.netlas.target.cobaltstrike;

import c2.search.netlas.scheme.Host;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

final class Utils {

  private Utils() {}

  public static int getDataLength(final Host host) throws UnknownHostException, IOException {
    try (Socket socket = new Socket(host.getTarget(), host.getPort());
        InputStream inputStream = socket.getInputStream()) {
      return getDataLength(inputStream);
    }
  }

  public static int getDataLength(final InputStream inputStream) throws IOException {
    final int chunk = 1024;
    final byte[] buffer = new byte[chunk];
    int len;
    int totalLen = 0;
    while (true) {
      len = inputStream.read(buffer);
      if (len == -1) {
        break;
      }
      totalLen += len;
    }
    return totalLen;
  }

  public static int sendHttpRequest(final Host host, final String ua, final String path)
      throws IOException {
    final String urlString = "http://" + host.getTarget() + ":" + host.getPort() + path;
    final URL url = new URL(urlString);
    final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    if (!ua.isEmpty() || ua == null) {
      connection.setRequestProperty("User-Agent", "random");
    }
    final int statusCode = connection.getResponseCode();
    connection.disconnect();
    return statusCode;
  }
}
