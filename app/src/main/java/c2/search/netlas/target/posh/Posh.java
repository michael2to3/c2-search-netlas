package c2.search.netlas.target.posh;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;

@Detect(name = "Posh")
public class Posh {
  @Wire private Host host;

  public Posh() {}
}
