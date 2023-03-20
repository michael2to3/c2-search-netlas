package c2.search.netlas.target.metasploit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class SocketConnectionTest {
  @Mock private SocketConnection socketConnection;
  @Mock private Socket mockSocket;
  @Mock private OutputStream mockOutputStream;
  @Mock private InputStream mockInputStream;

  private final String id = "12345";

  @BeforeEach
  public void setUp() throws IOException {
    initMocks(this);
    socketConnection = new SocketConnection(mockSocket, id);
    when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
    when(mockSocket.getInputStream()).thenReturn(mockInputStream);
  }

  @Test
  public void testSendAndReceive() throws IOException {
    String expectedResponse = "echo " + id;
    byte[] responseBytes = expectedResponse.getBytes();
    when(mockInputStream.read(any(byte[].class))).thenReturn(responseBytes.length);
    when(mockInputStream.read(responseBytes)).thenReturn(responseBytes.length);
    when(mockOutputStream.toString()).thenReturn(expectedResponse);

    String actualResponse = socketConnection.sendAndReceive();

    assertEquals(expectedResponse, actualResponse);
    verify(mockOutputStream).write(responseBytes);
    verify(mockOutputStream).flush();
    verify(mockInputStream).read(responseBytes);
  }

  @Test
  public void testClose() throws IOException {
    socketConnection.close();
    verify(mockSocket).close();
  }
}
