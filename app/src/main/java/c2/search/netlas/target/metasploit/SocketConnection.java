package c2.search.netlas.target.metasploit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketConnection implements AutoCloseable {
  private final String id;
  private final Socket socket;

  public SocketConnection(String host, int port, String id) throws IOException {
    this.socket = new Socket(host, port);
    this.id = id;
  }

  public String sendAndReceive() throws IOException {
    String message = "echo " + id;
    OutputStream output = socket.getOutputStream();
    InputStream input = socket.getInputStream();

    output.write(message.getBytes());
    output.flush();

    byte[] response = new byte[1024];
    int responseLength = input.read(response);
    return new String(response, 0, responseLength, StandardCharsets.UTF_8);
  }

  public void close() throws IOException {
    socket.close();
  }
}
