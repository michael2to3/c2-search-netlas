package c2.search.netlas.target.merlin;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;

@Detect(name = "Merlin")
public class Merlin {
  @Wire private Host host;

  public Merlin() {}
}
