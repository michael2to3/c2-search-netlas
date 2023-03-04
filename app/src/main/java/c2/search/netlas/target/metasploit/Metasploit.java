package c2.search.netlas.target.metasploit;

import c2.search.netlas.scheme.DetectResponse;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.Target;

public class Metasploit implements Target {

  @Override
  public DetectResponse run(final Host host) {
    return null;
  }

  private boolean defaultBodyResponse(final Host host) {
    return "<html><body><h1>It works!</h1></body></html>" == host.getHost();
  }
}
