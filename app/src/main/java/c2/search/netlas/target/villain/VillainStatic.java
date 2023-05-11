package c2.search.netlas.target.villain;

import c2.search.netlas.analyze.StaticData;
import c2.search.netlas.annotation.Static;
import java.util.Arrays;
import java.util.List;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;
import netlas.java.scheme.Issuer;
import netlas.java.scheme.Subject;

@Static(name = "Villain")
public class VillainStatic implements StaticData {

  public VillainStatic() {}

  @Override
  public List<String> getJarm() {
    final String jarm = "2ad2ad0002ad2ad22c42d42d000000faabb8fd156aa8b4d8a37853e1063261";
    return Arrays.asList(jarm);
  }

  @Override
  public List<Certificate> getCertificate() {
    // * subject: C=AU; ST=Some-State; O=Internet Widgits Pty Ltd
    // * issuer: C=AU; ST=Some-State; O=Internet Widgits Pty Ltd
    final Subject subject = new Subject();
    final var country = Arrays.asList("AU");
    final var state = Arrays.asList("Some-State");
    final var organization = Arrays.asList("Internet Widgits Pty Ltd");
    subject.setCountry(country);
    subject.setLocality(state);
    subject.setOrganization(organization);
    final Issuer issuer = new Issuer();
    issuer.setCountry(country);
    issuer.setLocality(state);
    issuer.setOrganization(organization);
    final Certificate certificate = new Certificate();
    certificate.setIssuer(issuer);
    certificate.setSubject(subject);
    return Arrays.asList(certificate);
  }

  @Override
  public List<Integer> getPort() {
    return List.of();
  }

  @Override
  public List<Headers> getHeader() {
    final Headers headers = new Headers();
    headers.setHeader("server", List.of("BaseHTTP"));
    return Arrays.asList(headers);
  }

  @Override
  public List<String> getBodyAsSha256() {
    return List.of();
  }
}
