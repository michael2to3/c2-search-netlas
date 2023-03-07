package c2.search.netlas.target;

import c2.search.netlas.scheme.Host;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import netlas.java.APIException;
import netlas.java.Netlas;

class NetlasWrapper {
  private Host host;
  private Netlas netlas;
  private JsonNode response;

  public NetlasWrapper(final String api, final Host host)
      throws JsonMappingException, JsonProcessingException, APIException {
    this.netlas = new Netlas(api);
    this.host = host;
    getResponse();
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

  public void setResponse(JsonNode response) {
    this.response = response;
  }

  public List<String> getDnsName() {
    var commonName =
        this.response
            .get("items")
            .get(0)
            .get("data")
            .get("certificate")
            .get("subject")
            .get("common_name");
    List<String> dnsNames = new ArrayList<String>();
    commonName.forEach(item -> dnsNames.add(item.asText()));
    return dnsNames;
  }

  private void getResponse() throws JsonMappingException, JsonProcessingException, APIException {
    if (response != null) {
      return;
    }

    var query = host.getHost();
    var datatype = "response";
    var page = 0;
    var indices = "";
    var fields = "";
    var excludeFields = false;
    this.response = this.netlas.search(query, datatype, page, indices, fields, excludeFields);
  }
}
