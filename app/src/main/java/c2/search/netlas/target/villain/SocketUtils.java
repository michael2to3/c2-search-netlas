package c2.search.netlas.target.villain;

import c2.search.netlas.scheme.Host;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

final class SocketUtils {
  private SocketUtils() {}

  public static String getSocketResponse(final Host host) throws IOException {
    try (Socket socket = new Socket(host.getTarget(), host.getPort());
        InputStreamReader input = new InputStreamReader(socket.getInputStream());
        BufferedReader in = new BufferedReader(input)) {
      final StringBuilder response = new StringBuilder();
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
}
