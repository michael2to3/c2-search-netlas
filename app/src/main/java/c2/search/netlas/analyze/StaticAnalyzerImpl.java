package c2.search.netlas.analyze;

import c2.search.netlas.scheme.Results;
import java.util.List;
import netlas.java.Netlas;

class StaticAnalyzerImpl implements StaticAnalyzer {
  private Netlas netlas;

  public StaticAnalyzerImpl(final Netlas netlas) {
    this.netlas = netlas;
  }

  @Override
  public Results analyze(final List<Static> data) {
    return null;
  }

  @Override
  public Results analyze(final Static data) {
    return null;
  }

  private String generateQuery(final Static data) {
    StringBuilder query = new StringBuilder();
    return null;
  }
}
