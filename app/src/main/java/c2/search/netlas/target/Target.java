package c2.search.netlas.target;

import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.DetectResponse;

public interface Target {
  public DetectResponse run(final Host host);
}
