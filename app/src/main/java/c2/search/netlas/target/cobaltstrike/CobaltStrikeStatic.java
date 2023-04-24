package c2.search.netlas.target.cobaltstrike;

import c2.search.netlas.analyze.Static;
import c2.search.netlas.annotation.Detect;
import java.util.ArrayList;
import java.util.List;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;
import netlas.java.scheme.Issuer;
import netlas.java.scheme.Subject;

@Detect(name = "CobaltStrike")
class CobaltStrikeStatic implements Static {
  @Override
  public List<String> getJarm() {
    return List.of("2ad2ad16d2ad2ad00042d42d00042ddb04deffa1705e2edc44cae1ed24a4da");
  }

  private Certificate generateCert(
      final String country,
      final String province,
      final String locality,
      final String organization,
      final String unit,
      final String commonName) {
    Certificate cert = new Certificate();
    Subject subject = new Subject();
    Issuer issuer = new Issuer();

    subject.setCountry(List.of(country));
    subject.setProvince(List.of(province));
    subject.setLocality(List.of(locality));
    subject.setOrganization(List.of(organization));
    subject.setOrganizationalUnit(List.of(unit));
    subject.setCommonName(List.of(commonName));

    issuer.setCountry(List.of(country));
    issuer.setLocality(List.of(locality));
    issuer.setOrganization(List.of(organization));
    issuer.setOrganizationalUnit(List.of(unit));
    issuer.setCommonName(List.of(commonName));

    cert.setIssuer(issuer);
    cert.setSubject(subject);

    return cert;
  }

  @Override
  public List<Certificate> getCertificate() {
    Certificate teamserver =
        generateCert(
            "US",
            "Washington",
            "Redmond",
            "Microsoft Corporation",
            "Microsoft Corporation",
            "Outlook.live.com");
    Certificate listener = generateCert("", "", "", "", "", "");
    return List.of(teamserver, listener);
  }

  @Override
  public List<Integer> getPort() {
    return List.of(41337);
  }

  @Override
  public List<Headers> getHeader() {
    Headers headers = new Headers();
    headers.setHeader("server", new ArrayList<>());
    headers.setHeader("content-length", List.of("0"));
    headers.setHeader("content-type", List.of("text/plain"));
    return List.of(headers);
  }

  @Override
  public List<String> getBodyAsSha256() {
    return null;
  }
}
