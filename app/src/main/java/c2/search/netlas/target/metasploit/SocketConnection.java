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
  private final InputStream inputStream;
  private final OutputStream outputStream;

  public SocketConnection(final Socket socket, final String trigger) {
    this.socket = socket;
    this.trigger = trigger;

    InputStream input = null;
    OutputStream output = null;
    try {
      input = socket.getInputStream();
      output = socket.getOutputStream();
    } catch (IOException e) {
      LOGGER.error("Failed to get input/output streams", e);
    }
    this.inputStream = input;
    this.outputStream = output;
  }

  public String sendAndReceive() {
    final String message = "echo " + trigger;
    String response = null;

    try (OutputStream output = outputStream;
        InputStream input = inputStream) {
      output.write(message.getBytes());
      output.flush();

      final byte[] bresponse = new byte[BUFFER_SIZE];
      final int responseLength = input.read(bresponse);
      response = new String(bresponse, 0, responseLength, StandardCharsets.UTF_8);
    } catch (IOException e) {
      LOGGER.error("Failed to send message", e);
    }
    return response;
  }

  @Override
  public void close() throws IOException {
    if (inputStream != null) {
      inputStream.close();
    }
    if (outputStream != null) {
      outputStream.close();
    }
    socket.close();
  }
}
