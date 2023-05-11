package c2.search.netlas.query;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Issuer;
import netlas.java.scheme.Subject;
import org.junit.jupiter.api.Test;

public class CertificateBuilderTest {
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

  @Test
  public void testBuild() {
    Certificate cert = getCert();

    CertificateBuilder builder = new CertificateBuilder(cert);

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
  }

  @Test
  public void testWithoutSubject() {
    Certificate cert = getCert();
    cert.setSubject(null);

    CertificateBuilder builder = new CertificateBuilder(cert);

    String query = builder.build();
    assertFalse(query.replaceAll("\\s{2,}", " ").contains("AND AND"));
    assertFalse(query.trim().substring(query.length() - 3).equals("AND"));
    assertFalse(query.trim().substring(0, 3).equals("AND"));
    assertTrue(query.contains("certificate.issuer.country:\"RU\""));
    assertTrue(query.contains("certificate.issuer.locality:\"Moscow\""));
    assertTrue(query.contains("certificate.issuer.organization:\"Netlas\""));
    assertTrue(query.contains("certificate.issuer.organizational_unit:\"Netlas\""));
    assertTrue(query.contains("certificate.issuer.province:\"Moscow\""));
  }

  @Test
  public void testWithoutIssuer() {
    Certificate cert = getCert();
    cert.setIssuer(null);

    CertificateBuilder builder = new CertificateBuilder(cert);

    String query = builder.build();
    assertFalse(query.replaceAll("\\s{2,}", " ").contains("AND AND"));
    assertFalse(query.trim().substring(query.length() - 3).equals("AND"));
    assertFalse(query.trim().substring(0, 3).equals("AND"));
    assertTrue(query.contains("certificate.subject.country:\"RU\""));
    assertTrue(query.contains("certificate.subject.locality:\"Moscow\""));
    assertTrue(query.contains("certificate.subject.organization:\"Netlas\""));
    assertTrue(query.contains("certificate.subject.organizational_unit:\"Netlas\""));
    assertTrue(query.contains("certificate.subject.province:\"Moscow\""));
  }

  @Test
  public void testNullFields() {
    List<String> base = new ArrayList<>();
    base.add(null);
    List<String> country = base;
    List<String> locality = base;
    List<String> organization = base;
    List<String> organizationUnit = base;
    List<String> province = base;

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

    CertificateBuilder builder = new CertificateBuilder(cert);

    String query = builder.build();
    assertFalse(query.replaceAll("\\s{2,}", " ").contains("AND AND"));
    assertFalse(query.trim().substring(query.length() - 3).equals("AND"));
    assertFalse(query.trim().substring(0, 3).equals("AND"));
    assertTrue(query.contains("certificate.subject.country:*"));
    assertTrue(query.contains("certificate.subject.locality:*"));
    assertTrue(query.contains("certificate.subject.organization:*"));
    assertTrue(query.contains("certificate.subject.organizational_unit:*"));
    assertTrue(query.contains("certificate.subject.province:*"));
    assertTrue(query.contains("certificate.issuer.country:*"));
    assertTrue(query.contains("certificate.issuer.locality:*"));
    assertTrue(query.contains("certificate.issuer.organization:*"));
    assertTrue(query.contains("certificate.issuer.organizational_unit:*"));
    assertTrue(query.contains("certificate.issuer.province:*"));
  }

  @Test
  public void testEmptyFields() {
    List<String> base = List.of("");
    List<String> country = base;
    List<String> locality = base;
    List<String> organization = base;
    List<String> organizationUnit = base;
    List<String> province = base;

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

    CertificateBuilder builder = new CertificateBuilder(cert);

    String query = builder.build();
    assertFalse(query.replaceAll("\\s{2,}", " ").contains("AND AND"));
    assertFalse(query.trim().substring(query.length() - 3).equals("AND"));
    assertFalse(query.trim().substring(0, 3).equals("AND"));
    assertTrue(query.contains("certificate.subject.country:\"\""));
    assertTrue(query.contains("certificate.subject.locality:\"\""));
    assertTrue(query.contains("certificate.subject.organization:\"\""));
    assertTrue(query.contains("certificate.subject.organizational_unit:\"\""));
    assertTrue(query.contains("certificate.subject.province:\"\""));
    assertTrue(query.contains("certificate.issuer.country:\"\""));
    assertTrue(query.contains("certificate.issuer.locality:\"\""));
    assertTrue(query.contains("certificate.issuer.organization:\"\""));
    assertTrue(query.contains("certificate.issuer.organizational_unit:\"\""));
    assertTrue(query.contains("certificate.issuer.province:\"\""));
  }

  @Test
  public void testWithMultiFields() {
    List<String> base = List.of("RU", "Moscow");
    List<String> country = base;
    List<String> locality = base;
    List<String> organization = base;
    List<String> organizationUnit = base;
    List<String> province = base;

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

    CertificateBuilder builder = new CertificateBuilder(cert);

    String query = builder.build();
    assertFalse(query.replaceAll("\\s{2,}", " ").contains("AND AND"));
    assertFalse(query.trim().substring(query.length() - 3).equals("AND"));
    assertFalse(query.trim().substring(0, 3).equals("AND"));
    assertTrue(
        query.contains(
            "certificate.subject.country:\"RU\" OR certificate.subject.country:\"Moscow\""));
    assertTrue(query.contains("certificate.subject.country:\"RU\""));
    assertTrue(query.contains("certificate.subject.locality:\"RU\""));
    assertTrue(query.contains("certificate.subject.organization:\"RU\""));
    assertTrue(query.contains("certificate.subject.organizational_unit:\"RU\""));
    assertTrue(query.contains("certificate.subject.province:\"RU\""));
    assertTrue(query.contains("certificate.issuer.country:\"RU\""));
    assertTrue(query.contains("certificate.issuer.locality:\"RU\""));
    assertTrue(query.contains("certificate.issuer.organization:\"RU\""));
    assertTrue(query.contains("certificate.issuer.organizational_unit:\"RU\""));
    assertTrue(query.contains("certificate.issuer.province:\"Moscow\""));
    assertTrue(query.contains("certificate.subject.country:\"Moscow\""));
    assertTrue(query.contains("certificate.subject.locality:\"Moscow\""));
    assertTrue(query.contains("certificate.subject.organization:\"Moscow\""));
    assertTrue(query.contains("certificate.subject.organizational_unit:\"Moscow\""));
    assertTrue(query.contains("certificate.subject.province:\"Moscow\""));
    assertTrue(query.contains("certificate.issuer.country:\"Moscow\""));
    assertTrue(query.contains("certificate.issuer.locality:\"Moscow\""));
    assertTrue(query.contains("certificate.issuer.organization:\"Moscow\""));
    assertTrue(query.contains("certificate.issuer.organizational_unit:\"Moscow\""));
    assertTrue(query.contains("certificate.issuer.province:\"Moscow\""));
  }
}
