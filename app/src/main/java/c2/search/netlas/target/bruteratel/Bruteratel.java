package c2.search.netlas.target.bruteratel;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Detect(name = "Bruteratel")
public class Bruteratel {
  @Wire private Host host;

  public Bruteratel() {}

  @Test(extern = true)
  public boolean badNotFound() throws IOException, NoSuchAlgorithmException {
    final int success = 200;
    final String sha256notfound =
        "a469ab4ca4e55bf547566e9ebfa1b809c933207e9d558156bc0c4252b17533fe";
    return Utils.testEndpoint(host.toString(), success, sha256notfound);
  }

  @Test(extern = true)
  public boolean redirect() throws IOException, NoSuchAlgorithmException {
    final int redirect = 301;
    final String sha256redirect =
        "ca0b9ce4cdb60ddeaa18227ffd26789709564d3ec1df5feb9a0f61ad0ed6a633";
    return Utils.testEndpoint(host.toString() + "//", redirect, sha256redirect);
  }
}
