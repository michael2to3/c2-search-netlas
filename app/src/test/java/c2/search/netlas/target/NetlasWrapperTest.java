package c2.search.netlas.target;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import c2.search.netlas.scheme.Host;
import java.util.List;
import netlas.java.exception.NetlasRequestException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NetlasWrapperTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(NetlasWrapperTest.class);
  private static final String API = System.getenv("API_KEY");
  private static final Host HOST =
      Host.newBuilder().setTarget("vk.com").setPort(443).setPath("/").build();
  private static NetlasWrapper netlas;

  @BeforeAll
  static void setup() throws NetlasRequestException {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Use API key: {}", API);
    }
    netlas = new NetlasWrapper(API, HOST);
  }

  @Test
  void testGetJarm() {
    String jarm = netlas.getJarm();
    assertNotNull(jarm);
    assertNotEquals("", jarm);
    assertTrue(jarm.length() == 62);
  }

  @Test
  void testGets() {
    String responseBody = netlas.getBody();
    assertNotNull(responseBody);
    assertNotEquals("", responseBody);
    assertTrue(responseBody.length() > 0);

    var headers = netlas.getHeaders();
    assertNotNull(headers);

    var servers = netlas.getServers();
    assertNotNull(servers);
    assertNotEquals(0, servers.size());
    assertNotNull(servers.get(0));
    assertNotEquals("", servers.get(0));

    var statusCode = netlas.getStatusCode();
    assertEquals(200, statusCode);

    var contentType = netlas.getContentType();
    assertNotNull(contentType);
    assertTrue(contentType.size() > 0);
    assertNotNull(contentType.get(0));
    assertNotEquals("", contentType.get(0));
  }

  @Test
  void testGetCertSubjectCountry() {
    List<String> subjectCountry = netlas.getCertSubjectCountry();
    assertNotNull(subjectCountry);
    assertNotEquals(0, subjectCountry.size());
    assertNotNull(subjectCountry.get(0));
    assertNotEquals("", subjectCountry.get(0));
  }

  @Test
  void testGetCertSubjectOrganizationUnit() throws NetlasRequestException {
    Host host = Host.newBuilder().setTarget("fadich.com").setPort(443).build();
    NetlasWrapper netlas = new NetlasWrapper(API, host);
    List<String> subjectOrganizationUnit = netlas.getCertSubjectOrganizationUnit();
    assertNotNull(subjectOrganizationUnit);
    assertNotEquals(0, subjectOrganizationUnit.size());
    assertNotNull(subjectOrganizationUnit.get(0));
    assertNotEquals("", subjectOrganizationUnit.get(0));
  }

  @Test
  void testGetCertSubjectOrganization() {
    List<String> subjectOrganization = netlas.getCertSubjectOrganization();
    assertNotNull(subjectOrganization);
    assertNotEquals(0, subjectOrganization.size());
    assertNotNull(subjectOrganization.get(0));
    assertNotEquals("", subjectOrganization.get(0));
  }

  @Test
  void testGetCertSubjectLocality() {
    List<String> subjectLocality = netlas.getCertSubjectLocality();
    assertNotNull(subjectLocality);
    assertNotEquals(0, subjectLocality.size());
    assertNotNull(subjectLocality.get(0));
    assertNotEquals("", subjectLocality.get(0));
  }

  @Test
  void testGetCertSubjectProvince() {
    List<String> subjectProvince = netlas.getCertSubjectProvince();
    assertNotNull(subjectProvince);
    assertNotEquals(0, subjectProvince.size());
    assertNotNull(subjectProvince.get(0));
    assertNotEquals("", subjectProvince.get(0));
  }

  @Test
  void testGetCertStartDate() {
    String certStartDate = netlas.getCertStartDate();
    assertNotNull(certStartDate);
    assertNotEquals("", certStartDate);
  }

  @Test
  void testGetCertEndDate() {
    String certEndDate = netlas.getCertEndDate();
    assertNotNull(certEndDate);
    assertNotEquals("", certEndDate);
  }

  @Test
  void testGetCertIssuerCommonName() {
    List<String> issuerCommonName = netlas.getCertIssuerCommonName();
    assertNotNull(issuerCommonName);
    assertNotEquals(0, issuerCommonName.size());
    assertNotNull(issuerCommonName.get(0));
    assertNotEquals("", issuerCommonName.get(0));
  }

  @Test
  void testGetCertIssuerOrganizationUnit() throws NetlasRequestException {
    Host host = Host.newBuilder().setTarget("infrapros.com").setPort(443).build();
    NetlasWrapper netlas = new NetlasWrapper(API, host);
    List<String> issuerOrganizationUnit = netlas.getCertIssuerOrganizationUnit();
    assertNotNull(issuerOrganizationUnit);
    assertNotEquals(0, issuerOrganizationUnit.size());
    assertNotNull(issuerOrganizationUnit.get(0));
    assertNotEquals("", issuerOrganizationUnit.get(0));
  }

  @Test
  void testGetCertIssuerCountry() {
    List<String> issuerCountry = netlas.getCertIssuerCountry();
    assertNotNull(issuerCountry);
    assertNotEquals(0, issuerCountry.size());
    assertNotNull(issuerCountry.get(0));
    assertNotEquals("", issuerCountry.get(0));
  }

  @Test
  void testGetCertIssuerOrganization() {
    List<String> issuerOrganization = netlas.getCertIssuerOrganization();
    assertNotNull(issuerOrganization);
    assertNotEquals(0, issuerOrganization.size());
    assertNotNull(issuerOrganization.get(0));
    assertNotEquals("", issuerOrganization.get(0));
  }

  @Test
  void testGetCertIssuerLocality() throws NetlasRequestException {
    Host host = Host.newBuilder().setTarget("fadich.com").setPort(443).build();
    NetlasWrapper netlas = new NetlasWrapper(API, host);
    List<String> issuerLocality = netlas.getCertIssuerLocality();
    assertNotNull(issuerLocality);
    assertNotEquals(0, issuerLocality.size());
    assertNotNull(issuerLocality.get(0));
    assertNotEquals("", issuerLocality.get(0));
  }

  void testGetCertIssuerProvince() {
    List<String> issuerProvince = netlas.getCertIssuerProvince();
    assertNotNull(issuerProvince);
    assertNotEquals(0, issuerProvince.size());
    assertNotNull(issuerProvince.get(0));
    assertNotEquals("", issuerProvince.get(0));
  }

  @Test
  void testGetContentLength() throws NetlasRequestException {
    Host host = new Host("google.com", 443);
    var netlas = new NetlasWrapper(API, host);
    List<Integer> contentLength = netlas.getContentLength();
    assertNotNull(contentLength);
    assertNotEquals(0, contentLength.size());
    assertNotNull(contentLength.get(0));
    assertNotEquals("", contentLength.get(0));
  }

  @Test
  void testGetCertSubjectCommonName() {
    List<String> subjectCommonName = netlas.getCertSubjectCommonName();
    assertNotNull(subjectCommonName);
    assertNotEquals(0, subjectCommonName.size());
    assertNotNull(subjectCommonName.get(0));
    assertNotEquals("", subjectCommonName.get(0));
  }

  @Test
  void testGetterAndSetter() throws NetlasRequestException {
    var netlas = new NetlasWrapper(API, HOST);
    assertNotNull(netlas.getHost());
    assertNotNull(netlas.getNetlas());
  }

  @Test
  void testHeaders() throws NetlasRequestException {
    var nn = new NetlasWrapper(API, new Host("vk.com", 443));
    var headers = nn.getHeaders();
    assertNotNull(headers);
    var server = nn.getServers();
    assertNotNull(server);
    assertNotEquals("", server.get(0));

    var status = nn.getStatusCode();
    assertTrue(status >= 200 && status < 600);
  }
}
