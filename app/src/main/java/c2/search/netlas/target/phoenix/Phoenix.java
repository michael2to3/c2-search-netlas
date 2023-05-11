package c2.search.netlas.target.phoenix;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Detect(name = "Phoenix")
public class Phoenix {
  @Wire private Host host;

  public Phoenix() {}

  @Test(extern = true)
  public boolean checkListenerResponse()
      throws KeyManagementException, NoSuchAlgorithmException, IOException {
    final String[] paths = {"/download/history.csv", "/download/users.csv", "/static/dump.sql"};
    final int accessDenied = 405;
    boolean result = true;
    for (final String path : paths) {
      result = NetworkUtils.getStatus(host, path) == accessDenied;
      if (!result) {
        break;
      }
    }
    return result;
  }
}
