package c2.search.netlas.target.metasploit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketConnection implements AutoCloseable {
  private static final Logger LOGGER = LoggerFactory.getLogger(SocketConnection.class);
  private static final int BUFFER_SIZE = 4096;
  private final String trigger;
  private final Socket socket;

  public SocketConnection(final Socket socket, final String trigger) {
    this.socket = socket;
    this.trigger = trigger;
  }

  public String sendAndReceive() {
    final String message = "echo " + trigger;
    String response = null;

    try (OutputStream output = socket.getOutputStream();
        InputStream input = socket.getInputStream()) {
      output.write(message.getBytes());
      output.flush();

      final byte[] bresponse = new byte[BUFFER_SIZE];
      final int responseLength = input.read(bresponse);
      response = new String(bresponse, 0, responseLength, StandardCharsets.UTF_8);
    } catch (IOException e) {
      LOGGER.warn("Failed to send message", e);
    }
    return response;
  }

  @Override
  public void close() throws IOException {
    socket.close();
  }
}
