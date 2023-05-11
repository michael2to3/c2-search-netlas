package c2.search.netlas.target.villain;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Detect(name = "Villain")
public class Villain {
  @Wire private Host host;

  public Villain() {}

  @Test(extern = true)
  public boolean checkHandlerTcp() throws IOException {
    final String base = "whoami";
    final String resp = SocketUtils.getSocketResponse(host);
    return resp.contains(base);
  }

  @Test(extern = true)
  public boolean checkPostResponse()
      throws IOException, KeyManagementException, NoSuchAlgorithmException {
    final String resp = Utils.sendPostRequest(host, "/");
    final String body = "Move on mate.";
    return resp.contains(body);
  }
}
