package c2.search.netlas.target.deimos;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Detect(name = "Deimos")
public class Deimos {
  @Wire private Host host;

  public Deimos() {}

  @Test(extern = true)
  public boolean checkRedirect() throws IOException {
    final List<String> pathes =
        Arrays.asList(
            "//",
            "/webdav/index.html",
            "/webadmin/index.html",
            "/tiny_mce/plugins/imagemanager/pages/im/index.html",
            "/templates/index.html",
            "/swagger/index.html",
            "/panel-administracion/index.html",
            "/modelsearch/index.html",
            "/mifs/user/index.html",
            "/index.html",
            "/demo/ejb/index.html",
            "/bb-admin/index.html");
    final int redirect = 301;
    boolean result = true;
    for (final String path : pathes) {
      final String[] resp = Utils.makeHttpRequest(host, path);
      final int status = Integer.parseInt(resp[0]);
      final String body = resp[1];
      if (status != redirect || !body.isEmpty()) {
        result = false;
        break;
      }
    }
    return result;
  }
}
