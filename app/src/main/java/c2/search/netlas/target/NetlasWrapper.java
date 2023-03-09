package c2.search.netlas.target;

import c2.search.netlas.scheme.Host;
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
  private static Map<Host, JsonNode> response;
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

  public void setResponse(JsonNode json) {
    if (response.containsKey(host)) {
      response.replace(host, json);
    } else {
      response.put(host, json);
    }
  }

  public List<String> getDnsName() throws JsonMappingException, JsonProcessingException {
    var commonName = getResponse().get("data").get("certificate").get("subject").get("common_name");
    List<String> dnsNames = new ArrayList<String>();
    commonName.forEach(item -> dnsNames.add(item.asText()));
    return dnsNames;
  }

  public String getJarm() throws JsonMappingException, JsonProcessingException {
    return getResponse().get("data").get("jarm").asText();
  }

  public String getResponseBody() throws JsonMappingException, JsonProcessingException {
    return getResponse().get("data").get("http").get("body").asText();
  }

  public String getResponseBodyAsSha256() throws JsonMappingException, JsonProcessingException {
    return getResponse().get("data").get("http").get("body_sha256").asText();
  }

  public JsonNode getResponse() throws JsonMappingException, JsonProcessingException {
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
    resp = resp.get("items").get(0);
    setResponse(resp);

    return resp;
  }
}
