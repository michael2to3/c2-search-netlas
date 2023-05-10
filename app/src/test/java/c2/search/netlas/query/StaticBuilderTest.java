package c2.search.netlas.query;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import c2.search.netlas.analyze.StaticData;
import java.util.List;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;
import netlas.java.scheme.Issuer;
import netlas.java.scheme.Subject;
import org.junit.jupiter.api.Test;

public class StaticBuilderTest {
  @Test
  public void testBuild() {
    StaticData data = new Data();
    StaticBuilder builder = new StaticBuilder(data);

    String query = builder.build();
    assertFalse(query.replaceAll("\\s{2,}", " ").contains("AND AND"));
    assertFalse(query.trim().substring(query.length() - 3).equals("AND"));
    assertFalse(query.trim().substring(0, 3).equals("AND"));
    assertTrue(query.contains("certificate.subject.country:\"RU\""));
    assertTrue(query.contains("certificate.subject.locality:\"Moscow\""));
    assertTrue(query.contains("certificate.subject.organization:\"Netlas\""));
    assertTrue(query.contains("certificate.subject.organizational_unit:\"Netlas\""));
    assertTrue(query.contains("certificate.subject.province:\"Moscow\""));
    assertTrue(query.contains("certificate.issuer.country:\"RU\""));
    assertTrue(query.contains("certificate.issuer.locality:\"Moscow\""));
    assertTrue(query.contains("certificate.issuer.organization:\"Netlas\""));
    assertTrue(query.contains("certificate.issuer.organizational_unit:\"Netlas\""));
    assertTrue(query.contains("certificate.issuer.province:\"Moscow\""));

    assertTrue(query.contains("jarm:\"jarm1\" OR"));
    assertTrue(query.contains("port:\"80\" OR"));
    assertTrue(query.contains("http.headers.server:\"server1\" OR"));
    assertTrue(query.contains("http.body_sha256:\"body1\" OR"));
  }

  private Certificate getCert() {
    List<String> country = List.of("RU");
    List<String> locality = List.of("Moscow");
    List<String> organization = List.of("Netlas");
    List<String> organizationUnit = List.of("Netlas");
    List<String> province = List.of("Moscow");

    Subject subject = new Subject();
    subject.setCountry(country); // certificate.subject.country:"RU"
    subject.setLocality(locality); // certificate.subject.locality:"Moscow"
    subject.setOrganization(organization); // certificate.subject.organization:"Netlas"
    subject.setOrganizationalUnit(
        organizationUnit); // certificate.subject.organizational_unit:"Netlas"
    subject.setProvince(province); // certificate.subject.province:"Moscow"

    Issuer issuer = new Issuer();
    issuer.setCountry(country); // certificate.issuer.country:"RU"
    issuer.setLocality(locality); // certificate.issuer.locality:"Moscow"
    issuer.setOrganization(organization); // certificate.issuer.organization:"Netlas"
    issuer.setOrganizationalUnit(
        organizationUnit); // certificate.issuer.organizational_unit:"Netlas"
    issuer.setProvince(province); // certificate.issuer.province:"Moscow"

    Certificate cert = new Certificate();
    cert.setSubject(subject);
    cert.setIssuer(issuer);

    return cert;
  }

  public class Data implements StaticData {
    @Override
    public List<String> getJarm() {
      return List.of("jarm1", "jarm2", "jarm3");
    }

    @Override
    public List<Certificate> getCertificate() {
      var cert = getCert();
      cert.getSubject().setCountry(List.of("RU", "EU"));
      var cert2 = getCert();
      return List.of(cert, cert2);
    }

    @Override
    public List<Integer> getPort() {
      return List.of(80, 443);
    }

    @Override
    public List<Headers> getHeader() {
      Headers headers = new Headers();
      headers.setHeader("server", List.of("server1", "server2"));
      headers.setHeader("powered-by", List.of("powered-by1", "powered-by2"));
      return List.of(headers);
    }

    @Override
    public List<String> getBodyAsSha256() {
      return List.of("body1", "body2");
    }
  }
}
