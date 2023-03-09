package c2.search.netlas.target;

import c2.search.netlas.scheme.DetectResponse;

public interface Target {
  public void setNetlas(final NetlasWrapper netlas);

  public DetectResponse run();
}
