package c2.search.netlas.analyze;

import java.util.List;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;

public interface Static {
  public List<String> getJarm();

  public List<Certificate> getCertificate();

  public List<Integer> getPort();

  public List<Headers> getHeader();

  public List<String> getBodyAsSha256();
}
