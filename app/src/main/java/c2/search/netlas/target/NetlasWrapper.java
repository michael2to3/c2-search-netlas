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

public class NetlasWrapper {
  private static final Logger LOGGER = LoggerFactory.getLogger(NetlasWrapper.class);
  private Map<Host, JsonNode> response;
  private Host host;
  private Netlas netlas;

  public NetlasWrapper(final String api, final Host host)
      throws JsonMappingException, JsonProcessingException {
    this.netlas = new Netlas(api);
    this.host = host;
    response = new HashMap<>();
  }

  public Host getHost() {
    return host;
  }

  public void setHost(Host host) {
    this.host = host;
  }

  public Netlas getNetlas() {
    return netlas;
  }

  public void setNetlas(Netlas netlas) {
    this.netlas = netlas;
  }

  public void set(JsonNode json) {
    if (response.containsKey(host)) {
      response.replace(host, json);
    } else {
      response.put(host, json);
    }
  }

  public List<String> getDnsName() throws JsonMappingException, JsonProcessingException {
    var commonName = getLast(".data.certificate.subject.common_name");
    List<String> dnsNames = new ArrayList<String>();
    commonName.forEach(item -> dnsNames.add(item.asText()));
    return dnsNames;
  }

  public String getJarm() throws JsonMappingException, JsonProcessingException {
    return getLast(".data.jarm").asText();
  }

  public String getBody() throws JsonMappingException, JsonProcessingException {
    return getLast(".data.http.body").asText();
  }

  public String getBodyAsSha256() throws JsonMappingException, JsonProcessingException {
    return getLast(".data.http.body_sha256").asText();
  }

  public JsonNode getItem(int i) throws JsonMappingException, JsonProcessingException {
    return get().get(i);
  }

  public JsonNode getLast(String keyPath) throws JsonMappingException, JsonProcessingException {
    return getLast(keyPath, 0);
  }

  protected String formatKey(String key) {
    return key.replace('.', '/');
  }

  protected JsonNode getNodeFromItem(JsonNode item, String key) {
    JsonPointer pointer = JsonPointer.compile(formatKey(key));
    JsonNode node = item.at(pointer);
    if (!node.isMissingNode()) {
      return node;
    }
    return null;
  }

  public JsonNode getLast(String keyPath, int skip)
      throws JsonMappingException, JsonProcessingException {
    LOGGER.info("getLastHas: {}", keyPath);
    int count = get().size();
    for (int i = skip; i < count; ++i) {
      JsonNode value = getNodeFromItem(getItem(i), keyPath);
      if (value != null) {
        return value;
      }
    }
    return null;
  }

  public JsonNode get() throws JsonMappingException, JsonProcessingException {
    if (response.containsKey(host)) {
      return response.get(host);
    }

    var query = String.format("host:%s AND port:%s", host.getTarget(), host.getPort());
    var datatype = "responses";
    var page = 0;
    var indices = "";
    var fields = "";
    var excludeFields = false;
    var resp = this.netlas.search(query, datatype, page, indices, fields, excludeFields);
    resp = resp.get("items");
    set(resp);

    return resp;
  }

  public JsonNode getHeaders() throws JsonMappingException, JsonProcessingException {
    return getLast(".data.http.headers");
  }

  public List<String> getServers() throws JsonMappingException, JsonProcessingException {
    List<String> servers = new ArrayList<>();
    JsonNode items = getLast(".data.http.headers.server");
    if (items == null) {
      return null;
    }

    items.forEach(
        item -> {
          servers.add(item.asText());
        });
    return servers;
  }

  public int getStatusCode() throws JsonMappingException, JsonProcessingException {
    return getLast(".data.http.status_code").asInt();
  }
}
