package c2.search.netlas.target.sliver;

import c2.search.netlas.analyze.Static;
import c2.search.netlas.annotation.Detect;
import java.util.List;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;
import netlas.java.scheme.Issuer;
import netlas.java.scheme.Subject;

@Detect(name = "Sliver")
class SliverStatic implements Static {
  @Override
  public List<String> getJarm() {
    final String jarm = "3fd21c00000000021c43d21c21c43d3795b2a696610c3ae44909dcdcb797f2";
    return List.of(jarm);
  }

  @Override
  public List<Certificate> getCertificate() {
    var country = List.of("CA");
    var province = List.of("Newfoundland and Labrador");
    var locality = List.of("Mount Pearl");
    var organization = List.of("College");
    var organizationUnit = List.of("distant lettuce, incorporated");
    var commonName = List.of("localhost");
    Subject subject = new Subject();
    subject.setCountry(country);
    subject.setProvince(province);
    subject.setLocality(locality);
    subject.setOrganization(organization);
    subject.setOrganizationalUnit(organizationUnit);
    subject.setCommonName(commonName);
    Certificate teamserver = new Certificate();
    teamserver.setSubject(subject);

    Issuer issuer = new Issuer();
    issuer.setCommonName(List.of("operators"));
    subject = new Subject();
    subject.setCommonName(List.of("multiplayer"));
    Certificate listener = new Certificate();
    listener.setIssuer(issuer);
    listener.setSubject(subject);

    return List.of(teamserver, listener);
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
    return null;
  }
}
