package c2.search.netlas.analyze;

import c2.search.netlas.scheme.Results;

public interface StaticAnalyzer {
  public abstract Results analyze(StaticData data);
}
