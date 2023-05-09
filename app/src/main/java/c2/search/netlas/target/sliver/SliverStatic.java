package c2.search.netlas.target.sliver;

import c2.search.netlas.analyze.StaticData;
import c2.search.netlas.annotation.Static;
import java.util.List;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;
import netlas.java.scheme.Issuer;
import netlas.java.scheme.Subject;

@Static(name = "Sliver")
public class SliverStatic implements StaticData {
  @Override
  public List<String> getJarm() {
    final String jarm = "3fd21c00000000021c43d21c21c43d3795b2a696610c3ae44909dcdcb797f2";
    return List.of(jarm);
  }

  @Override
  public List<Certificate> getCertificate() {
    final var country = List.of("CA");
    final var province = List.of("Newfoundland and Labrador");
    final var locality = List.of("Mount Pearl");
    final var organization = List.of("College");
    final var organizationUnit = List.of("distant lettuce, incorporated");
    final var commonName = List.of("localhost");
    Subject subject = new Subject();
    subject.setCountry(country);
    subject.setProvince(province);
    subject.setLocality(locality);
    subject.setOrganization(organization);
    subject.setOrganizationalUnit(organizationUnit);
    subject.setCommonName(commonName);
    final Certificate teamserver = new Certificate();
    teamserver.setSubject(subject);

    final Issuer issuer = new Issuer();
    issuer.setCommonName(List.of("operators"));
    subject = new Subject();
    subject.setCommonName(List.of("multiplayer"));
    final Certificate listener = new Certificate();
    listener.setIssuer(issuer);
    listener.setSubject(subject);

    return List.of(teamserver, listener);
  }

  @Override
  public List<Integer> getPort() {
    return List.of();
  }

  @Override
  public List<Headers> getHeader() {
    return List.of();
  }

  @Override
  public List<String> getBodyAsSha256() {
    return List.of();
  }
}
