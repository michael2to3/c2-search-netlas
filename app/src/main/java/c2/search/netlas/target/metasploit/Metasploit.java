package c2.search.netlas.target.metasploit;

import c2.search.netlas.annotation.BeforeAll;
import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.Version;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Locale;

@Detect(name = "Metasploit")
public class Metasploit {
  private static final String SHELL_ID = "shell";
  final static int STATUS_SUCCESFULL = 200;
  @Wire private Host host;
  @Wire private NetlasWrapper netlasWrapper;
  @Wire private Socket socket;
  private SocketConnection socketConnection;

  public void setHost(final Host lhost) {
    this.host = lhost;
  }

  public void setNetlasWrapper(final NetlasWrapper netlasWrapper) {
    this.netlasWrapper = netlasWrapper;
  }

  public void setSocket(final Socket socket) {
    this.socket = socket;
  }

  public void setSocketConnection(final SocketConnection socketConnection) {
    this.socketConnection = socketConnection;
  }

  @BeforeAll
  public void init() throws IOException {
    socketConnection = new SocketConnection(socket, SHELL_ID);
  }

  @Test
  public Response checkDefaultBodyResponse() throws JsonMappingException, JsonProcessingException {
    String body = "";
    body = netlasWrapper.getBody();
    final String defaultBody = "It works!";
    final String defaultTagPayload = "echo";
    return new Response(body.contains(defaultBody) || body.contains(defaultTagPayload));
  }

  private boolean checkJarm(final String body, final List<String> jarms) {
    for (final String jarm : jarms) {
      if (body.contains(jarm)) {
        return true;
      }
    }
    return false;
  }

  @Test
  public Response checkJarm() throws JsonMappingException, JsonProcessingException {
    final List<String> jarmv5 =
        List.of("07d14d16d21d21d07c42d43d000000f50d155305214cf247147c43c0f1a823");
    final List<String> jarmv6 =
        List.of(
            "07d19d12d21d21d07c42d43d000000f50d155305214cf247147c43c0f1a823",
            "07b03b12b21b21b07b07b03b07b21b23aeefb38b723c523befb314af6e95ac",
            "07c03c12c21c21c07c07c03c07c21c23aeefb38b723c523befb314af6e95ac",
            "07d19d12d21d21d00007d19d07d21d0ae59125bcd90b8876b50928af8f6cd4");

    String responseJarm = "";
    responseJarm = netlasWrapper.getJarm();

    String minVersion = null;
    boolean detect = false;
    if (checkJarm(responseJarm, jarmv5)) {
      minVersion = "5.x.x";
      detect = true;
    }
    if (checkJarm(responseJarm, jarmv6)) {
      minVersion = "6.x.x";
      detect = true;
    }
    return new Response(detect, new Version(null, minVersion));
  }

  @Test
  public Response checkHeaders() throws JsonMappingException, JsonProcessingException {
    final List<String> servers = netlasWrapper.getServers();
    final String defaultServer = "apache";
    boolean hasDefaultServer = false;
    for (final String server : servers) {
      if (server.toLowerCase(Locale.getDefault()).contains(defaultServer)) {
        hasDefaultServer = true;
        break;
      }
    }

    int status = 0;
    status = netlasWrapper.getStatusCode();

    return new Response(hasDefaultServer && STATUS_SUCCESFULL == status);
  }

  @Test(extern = true)
  public Response checkBindShell() throws IOException {
    final String response = socketConnection.sendAndReceive();
    return new Response(response.contains(SHELL_ID));
  }
}
