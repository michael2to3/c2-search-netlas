package c2.search.netlas.target.havoc;

import c2.search.netlas.analyze.Static;
import c2.search.netlas.annotation.Detect;
import java.util.List;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;

@Detect(name = "Deimos")
class DeimosStatic implements Static {

  @Override
  public List<String> getJarm() {
    return List.of("3fd21b20d00000021c43d21b21b43de0a012c76cf078b8d06f4620c2286f5e");
  }

  @Override
  public List<Certificate> getCertificate() {
    return null;
  }

  @Override
  public List<Integer> getPort() {
    return null;
  }

  @Override
  public List<Headers> getHeader() {
    Headers headers = new Headers();
    headers.setHeader("x-ishavocframework", null);
    return List.of(headers);
  }

  @Override
  public List<String> getBodyAsSha256() {
    return List.of("b16e15764b8bc06c5c3f9f19bc8b99fa48e7894aa5a6ccdad65da49bbf564793");
  }
}
