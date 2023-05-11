package c2.search.netlas.target.posh;

import c2.search.netlas.analyze.StaticData;
import c2.search.netlas.annotation.Static;
import java.util.List;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;
import netlas.java.scheme.Issuer;
import netlas.java.scheme.Subject;

@Static(name = "Posh")
public class PoshStatic implements StaticData {
  public PoshStatic() {}

  @Override
  public List<String> getJarm() {
    final String jarm = "2ad2ad0002ad2ad22c42d42d000000faabb8fd156aa8b4d8a37853e1063261";
    return List.of(jarm);
  }

  @Override
  public List<Certificate> getCertificate() {
    final var country = List.of("US");
    final var province = List.of("Minnesota");
    final var locality = List.of("Minnetonka");
    final var organization = List.of("Pajfds");
    final var organizationUnit = List.of("Jethpro");
    final var commonName = List.of("P18055077");
    final Subject subject = new Subject();
    subject.setCountry(country);
    subject.setProvince(province);
    subject.setLocality(locality);
    subject.setOrganization(organization);
    subject.setOrganizationalUnit(organizationUnit);
    subject.setCommonName(commonName);
    final Issuer issue = new Issuer();
    issue.setCountry(country);
    issue.setProvince(province);
    issue.setLocality(locality);
    issue.setOrganization(organization);
    issue.setOrganizationalUnit(organizationUnit);
    issue.setCommonName(commonName);

    final Certificate cert = new Certificate();
    cert.setSubject(subject);
    cert.setIssuer(issue);

    return List.of(cert);
  }

  @Override
  public List<Integer> getPort() {
    return List.of();
  }

  @Override
  public List<Headers> getHeader() {
    final Headers header = new Headers();
    header.setHeader("Content-Type", List.of("text/html"));
    header.setHeader("Server", List.of("Apache"));
    return List.of(header);
  }

  @Override
  public List<String> getBodyAsSha256() {
    return List.of("f8bf41177a5f5e808a7ccb648b51080b031f15ca8018d91a576263d6cc626eb6");
  }
}
