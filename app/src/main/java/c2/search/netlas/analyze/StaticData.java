package c2.search.netlas.analyze;

import java.util.List;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;

public interface StaticData {
  public abstract List<String> getJarm();

  public abstract List<Certificate> getCertificate();

  public abstract List<Integer> getPort();

  public abstract List<Headers> getHeader();

  public abstract List<String> getBodyAsSha256();
}
