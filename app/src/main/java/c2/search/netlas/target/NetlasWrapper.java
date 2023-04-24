package c2.search.netlas.target;

import c2.search.netlas.scheme.Host;
import java.util.List;
import netlas.java.Netlas;
import netlas.java.exception.NetlasRequestException;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;
import netlas.java.scheme.Item;
import netlas.java.scheme.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper class for Netlas API that provides convenience methods for retrieving data from Netlas
 * responses.
 *
 * @deprecated This class is deprecated and will be removed in a future release. Use {@link Netlas}
 *     instead.
 */
@Deprecated
public class NetlasWrapper {
  private static final Logger LOGGER = LoggerFactory.getLogger(NetlasWrapper.class);
  private final Netlas netlas;
  private final Host host;
  private final Response response;

  public NetlasWrapper(final Netlas netlas, final Host host) throws NetlasRequestException {
    this.netlas = netlas;
    this.host = host;
    this.response = createResponse();
  }

  /**
   * Creates a new NetlasWrapper instance.
   *
   * @param api the Netlas API key
   * @param host the target host
   */
  public NetlasWrapper(final String api, final Host host) throws NetlasRequestException {
    this.netlas = Netlas.newBuilder().setApiKey(api).build();
    this.host = host;
    this.response = createResponse();
  }

  private Response createResponse() throws NetlasRequestException {
    final String query =
        String.format(
            "host:%s AND port:%s AND path:\"%s\"",
            host.getTarget(), host.getPort(), host.getPath());
    try {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Response successfully retrieved: {}", response);
      }
      return this.netlas.response(query, 0, null, null, false);
    } catch (final NetlasRequestException e) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Failed to retrieve response: {}", e.getMessage());
      }
      throw e;
    }
  }

  /**
   * Gets the target host.
   *
   * @return the target host
   */
  public Host getHost() {
    return host;
  }

  /**
   * Gets the Netlas instance used by this wrapper.
   *
   * @return the Netlas instance
   */
  public Netlas getNetlas() {
    return netlas;
  }

  /**
   * Gets the JARM fingerprint from the Netlas response.
   *
   * @return the JARM fingerprint
   */
  public String getJarm() {
    return response.getItems().get(0).getData().getJarm();
  }

  /**
   * Gets the HTTP response body from the Netlas response.
   *
   * @return the HTTP response body
   */
  public String getBody() {
    return response.getItems().get(0).getData().getHttp().getBody();
  }

  /**
   * Gets the SHA-256 hash of the HTTP response body from the Netlas response.
   *
   * @return the SHA-256 hash of the HTTP response body
   */
  public String getBodyAsSha256() {
    return response.getItems().get(0).getData().getHttp().getBodySha256();
  }

  /**
   * Returns the headers JsonNode for the current response.
   *
   * @return the headers JsonNode for the current response
   */
  public Headers getHeaders() {
    final List<Item> items = response.getItems();
    Headers headers = null;
    for (final Item item : items) {
      if (item != null
          && item.getData() != null
          && item.getData().getHttp() != null
          && item.getData().getHttp().getHeaders() != null) {
        headers = item.getData().getHttp().getHeaders();
        break;
      }
    }
    return headers;
  }

  /**
   * Returns a List of servers found in the headers for the current response.
   *
   * @return a List of servers found in the headers for the current response, or null if not found
   */
  public List<String> getServers() {
    return response.getItems().get(0).getData().getHttp().getHeaders().getHeader("server");
  }

  /**
   * Returns the status code for the current response.
   *
   * @return the status code for the current response
   */
  public int getStatusCode() {
    return response.getItems().get(0).getData().getHttp().getStatusCode();
  }

  public List<String> getContentType() {
    return response.getItems().get(0).getData().getHttp().getHeaders().getHeader("content-type");
  }

  public List<Integer> getContentLength() {
    return response
        .getItems()
        .get(0)
        .getData()
        .getHttp()
        .getHeaders()
        .getHeader("content-length")
        .stream()
        .mapToInt(Integer::parseInt)
        .boxed()
        .toList();
  }

  public List<String> getCertSubjectCommonName() {
    return getCertificate().getSubject().getCommonName();
  }

  public List<String> getCertSubjectCountry() {
    return getCertificate().getSubject().getCountry();
  }

  public List<String> getCertSubjectOrganizationUnit() {
    return getCertificate().getSubject().getOrganizationalUnit();
  }

  public List<String> getCertSubjectOrganization() {
    return getCertificate().getSubject().getOrganization();
  }

  public List<String> getCertSubjectLocality() {
    return getCertificate().getSubject().getLocality();
  }

  public List<String> getCertSubjectProvince() {
    return getCertificate().getSubject().getProvince();
  }

  public String getCertStartDate() {
    return getCertificate().getValidity().getStart();
  }

  public String getCertEndDate() {
    return getCertificate().getValidity().getEnd();
  }

  public List<String> getCertIssuerCommonName() {
    return getCertificate().getIssuer().getCommonName();
  }

  public List<String> getCertIssuerOrganizationUnit() {
    return getCertificate().getIssuer().getOrganizationalUnit();
  }

  public List<String> getCertIssuerCountry() {
    return getCertificate().getIssuer().getCountry();
  }

  public List<String> getCertIssuerOrganization() {
    return getCertificate().getIssuer().getOrganization();
  }

  public List<String> getCertIssuerLocality() {
    return getCertificate().getIssuer().getLocality();
  }

  public List<String> getCertIssuerProvince() {
    return getCertificate().getIssuer().getProvince();
  }

  private Certificate getCertificate() {
    final List<Item> items = response.getItems();
    Certificate certificate = null;
    for (final Item item : items) {
      if (item.getData() != null && item.getData().getCertificate() != null) {
        certificate = item.getData().getCertificate();
        break;
      }
    }
    return certificate;
  }
}
