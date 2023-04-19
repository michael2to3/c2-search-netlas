package c2.search.netlas.target.sliver;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import java.security.NoSuchAlgorithmException;

@Detect(name = "Sliver")
public class Sliver {
  @Wire private Host host;

  public Sliver() {}

  @Test(extern = true)
  public boolean checkRedirect() throws NoSuchAlgorithmException {
    final int redirect = 301;
    final int len = 0;
    return Utils.testEndpoint(host + "//", redirect, len);
  }
}
