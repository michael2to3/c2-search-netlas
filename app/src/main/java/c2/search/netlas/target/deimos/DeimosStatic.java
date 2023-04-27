package c2.search.netlas.target.deimos;

import c2.search.netlas.analyze.StaticData;
import c2.search.netlas.annotation.Static;
import java.util.List;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;
import netlas.java.scheme.Issuer;
import netlas.java.scheme.Subject;

@Static(name = "Deimos")
public class DeimosStatic implements StaticData {

  @Override
  public List<String> getJarm() {
    return List.of(
        "00000000000000000041d00000041d9535d5979f591ae8e547c5e5743e5b64",
        "1bd1bd1bd0001bd00041d1bd1bd41db0fe6e6bbf8c4edda78e3ec2bfb55687");
  }

  @Override
  public List<Certificate> getCertificate() {
    List<String> org = List.of("Acme Co");
    Certificate certificate = new Certificate();
    Subject subject = new Subject();
    subject.setOrganization(org);
    certificate.setSubject(subject);
    Issuer issuer = new Issuer();
    issuer.setOrganization(org);
    certificate.setIssuer(issuer);
    return List.of(certificate);
  }

  @Override
  public List<Integer> getPort() {
    return null;
  }

  @Override
  public List<Headers> getHeader() {
    return null;
  }

  @Override
  public List<String> getBodyAsSha256() {
    return List.of(
        "4bb3ee8361794353b8f76f97778fd377838b1354193b552cad5b3bf742cc9b34",
        "601d4cab0d7d8b3985b73f6ac6130f3b2a43a286d1f5350d8320de9ee501777c");
  }
}
