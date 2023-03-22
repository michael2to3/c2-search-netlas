package c2.search.netlas.target.metasploit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SocketConnectionTest {
  private static final String HOST = "localhost";
  private static final int PORT = 12345;
  private static final String ID = "test_id";
  private static final String RESPONSE = "response";

  @Mock private Socket socket;

  private InputStream inputStream;
  private OutputStream outputStream;
  private SocketConnection socketConnection;

  @BeforeEach
  void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);

    inputStream = new ByteArrayInputStream(RESPONSE.getBytes());
    outputStream = new ByteArrayOutputStream();

    when(socket.getInputStream()).thenReturn(inputStream);
    when(socket.getOutputStream()).thenReturn(outputStream);

    socketConnection = new SocketConnection(socket, ID);
  }

  @Test
  void testSendAndReceive() throws IOException {
    String response = socketConnection.sendAndReceive();
    assertEquals(RESPONSE, response, "Received response should match expected response");

    String sentMessage = outputStream.toString();
    assertEquals("echo " + ID, sentMessage, "Sent message should match expected message");
  }

  @Test
  void testClose() throws IOException {
    socketConnection.close();
  }
}
