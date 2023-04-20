package c2.search.netlas.analyze;

import c2.search.netlas.scheme.Results;
import java.util.List;

public interface StaticAnalyzer {
  public Results analyze(List<Static> data);

  public Results analyze(Static data);
}
