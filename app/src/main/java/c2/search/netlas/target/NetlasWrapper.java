package c2.search.netlas.target;

import c2.search.netlas.scheme.Host;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import netlas.java.Netlas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper class for Netlas API that provides convenience methods for retrieving data from Netlas
 * responses.
 */
public class NetlasWrapper {
  private static final Logger LOGGER = LoggerFactory.getLogger(NetlasWrapper.class);
  private final Map<Host, JsonNode> response;
  private final Host host;
  private final Netlas netlas;

  /**
   * Creates a new NetlasWrapper instance.
   *
   * @param api the Netlas API key
   * @param host the target host
   * @throws JsonMappingException if there is an error mapping JSON to Java objects
   * @throws JsonProcessingException if there is an error processing JSON
   */
  public NetlasWrapper(final String api, final Host host)
      throws JsonMappingException, JsonProcessingException {
    this.netlas = new Netlas(api);
    this.host = host;
    response = new HashMap<>();
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
   * Sets the Netlas response for the target host.
   *
   * @param json the Netlas response
   */
  public void set(final JsonNode json) {
    if (response.containsKey(host)) {
      response.replace(host, json);
    } else {
      response.put(host, json);
    }
  }

  /**
   * Gets a list of DNS names from the Netlas response.
   *
   * @return a list of DNS names
   * @throws JsonMappingException if there is an error mapping JSON to Java objects
   * @throws JsonProcessingException if there is an error processing JSON
   */
  public List<String> getDnsName() throws JsonMappingException, JsonProcessingException {
    final var commonName = getLast(".data.certificate.subject.common_name");
    final List<String> dnsNames = new ArrayList<>();
    commonName.forEach(item -> dnsNames.add(item.asText()));
    return dnsNames;
  }

  /**
   * Gets the JARM fingerprint from the Netlas response.
   *
   * @return the JARM fingerprint
   * @throws JsonMappingException if there is an error mapping JSON to Java objects
   * @throws JsonProcessingException if there is an error processing JSON
   */
  public String getJarm() throws JsonMappingException, JsonProcessingException {
    return getLast(".data.jarm").asText();
  }

  /**
   * Gets the HTTP response body from the Netlas response.
   *
   * @return the HTTP response body
   * @throws JsonMappingException if there is an error mapping JSON to Java objects
   * @throws JsonProcessingException if there is an error processing JSON
   */
  public String getBody() throws JsonMappingException, JsonProcessingException {
    return getLast(".data.http.body").asText();
  }

  /**
   * Gets the SHA-256 hash of the HTTP response body from the Netlas response.
   *
   * @return the SHA-256 hash of the HTTP response body
   * @throws JsonMappingException if there is an error mapping JSON to Java objects
   * @throws JsonProcessingException if there is an error processing JSON
   */
  public String getBodyAsSha256() throws JsonMappingException, JsonProcessingException {
    return getLast(".data.http.body_sha256").asText();
  }

  /**
   * Gets the Netlas response item at the specified index.
   *
   * @param item the index of the item to retrieve
   * @return the Netlas response item
   * @throws JsonMappingException if there is an error mapping JSON to Java objects
   * @throws JsonProcessingException if there is an error processing JSON
   */
  public JsonNode getItem(final int item) throws JsonMappingException, JsonProcessingException {
    return get().get(item);
  }

  /**
   * Returns the last JsonNode with the given keyPath, starting from the end of the list.
   *
   * @param keyPath the path of the key to search for
   * @return the last JsonNode with the given keyPath, or null if not found
   * @throws JsonMappingException if there is an issue with mapping json to objects
   * @throws JsonProcessingException if there is an issue with processing json
   */
  public JsonNode getLast(final String keyPath)
      throws JsonMappingException, JsonProcessingException {
    return getLast(keyPath, 0);
  }

  /**
   * Formats the given key by replacing "." with "/".
   *
   * @param key the key to format
   * @return the formatted key
   */
  protected String formatKey(final String key) {
    return key.replace('.', '/');
  }

  /**
   * Returns the JsonNode with the given key from the given item.
   *
   * @param item the item to search within
   * @param key the key to search for
   * @return the JsonNode with the given key, or null if not found
   */
  protected JsonNode getNodeFromItem(final JsonNode item, final String key) {
    final JsonPointer pointer = JsonPointer.compile(formatKey(key));
    return item.at(pointer);
  }

  /**
   * Returns the last JsonNode with the given keyPath, starting from the given skip index.
   *
   * @param keyPath the path of the key to search for
   * @param skip the number of items to skip from the start of the list
   * @return the last JsonNode with the given keyPath, or null if not found
   * @throws JsonMappingException if there is an issue with mapping json to objects
   * @throws JsonProcessingException if there is an issue with processing json
   */
  public JsonNode getLast(final String keyPath, final int skip)
      throws JsonMappingException, JsonProcessingException {
    LOGGER.info("getLastHas: {}", keyPath);
    final int count = get().size();
    JsonNode node = null;
    for (int i = skip; i < count; ++i) {
      final JsonNode value = getNodeFromItem(getItem(i), keyPath);
      if (value != null) {
        node = value;
        break;
      }
    }
    return node;
  }

  /**
   * Returns the response JsonNode for the current host. If the response has not yet been set, it
   * queries the NetlasWrapper and sets the response.
   *
   * @return the response JsonNode for the current host
   * @throws JsonMappingException if there is an issue with mapping json to objects
   * @throws JsonProcessingException if there is an issue with processing json
   */
  public JsonNode get() throws JsonMappingException, JsonProcessingException {
    if (response.containsKey(host)) {
      return response.get(host);
    }

    final var query = String.format("host:%s AND port:%s", host.getTarget(), host.getPort());
    final var datatype = "responses";
    final var page = 0;
    final var indices = "";
    final var fields = "";
    final var excludeFields = false;
    var resp = this.netlas.search(query, datatype, page, indices, fields, excludeFields);
    resp = resp.get("items");
    set(resp);

    return resp;
  }

  /**
   * Returns the headers JsonNode for the current response.
   *
   * @return the headers JsonNode for the current response
   * @throws JsonMappingException if there is an issue with mapping json to objects
   * @throws JsonProcessingException if there is an issue with processing json
   */
  public JsonNode getHeaders() throws JsonMappingException, JsonProcessingException {
    return getLast(".data.http.headers");
  }

  /**
   * Returns a List of servers found in the headers for the current response.
   *
   * @return a List of servers found in the headers for the current response, or null if not found
   * @throws JsonMappingException if there is an issue with mapping json to objects
   * @throws JsonProcessingException if there is an issue with processing json
   */
  public List<String> getServers() throws JsonMappingException, JsonProcessingException {
    final List<String> servers = new ArrayList<>();
    final JsonNode items = getLast(".data.http.headers.server");

    items.forEach(
        item -> {
          servers.add(item.asText());
        });
    return servers;
  }

  /**
   * Returns the status code for the current response.
   *
   * @return the status code for the current response
   * @throws JsonMappingException if there is an issue with mapping json to objects
   * @throws JsonProcessingException if there is an issue with processing json
   */
  public int getStatusCode() throws JsonMappingException, JsonProcessingException {
    return getLast(".data.http.status_code").asInt();
  }
}
