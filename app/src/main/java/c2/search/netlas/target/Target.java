package c2.search.netlas.target;

import c2.search.netlas.scheme.DetectResponse;
import c2.search.netlas.scheme.Host;

public interface Target {
  public DetectResponse run(final Host host);
}
