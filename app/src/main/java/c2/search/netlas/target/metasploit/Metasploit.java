package c2.search.netlas.target.metasploit;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import java.io.IOException;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Detect(name = "Metasploit")
public class Metasploit {
  private static final Logger LOGGER = LoggerFactory.getLogger(Metasploit.class);
  private static final int SOCKET_TIMEOUT_MS = 1000;
  private static final String SHELL_ID = "shell";
  @Wire private Host host;

  public Metasploit() {}

  @Test(extern = true)
  public boolean checkBindShell() {
    boolean result = false;
    try (Socket socket = new Socket(host.getTarget(), host.getPort());
        SocketConnection conn = new SocketConnection(socket, SHELL_ID)) {
      socket.setSoTimeout(SOCKET_TIMEOUT_MS);
      final String response = conn.sendAndReceive();
      result = response.contains(SHELL_ID);
    } catch (IOException e) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Failed to connect to {}:{}", host.getTarget(), host.getPort(), e);
      }
    }
    return result;
  }
}
