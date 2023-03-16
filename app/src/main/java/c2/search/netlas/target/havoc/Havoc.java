package c2.search.netlas.target.havoc;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Detect(name = "Havoc")
public class Havoc {
  private static final Logger LOGGER = LoggerFactory.getLogger(Havoc.class);

  @Wire private Host host;
  @Wire private NetlasWrapper netlasWrapper;

  public static Logger getLogger() {
    return LOGGER;
  }

  public Host getHost() {
    return host;
  }

  public void setHost(Host host) {
    this.host = host;
  }

  public NetlasWrapper getNetlasWrapper() {
    return netlasWrapper;
  }

  public void setNetlasWrapper(NetlasWrapper netlasWrapper) {
    this.netlasWrapper = netlasWrapper;
  }

  @Test
  public Response checkJarm() throws JsonMappingException, JsonProcessingException {
    String jarm = "3fd21b20d00000021c43d21b21b43de0a012c76cf078b8d06f4620c2286f5e";
    String rjarm = netlasWrapper.getJarm();
    return new Response(rjarm.equals(jarm));
  }

  @Test
  public Response checkDefaultBodyResponse() throws JsonMappingException, JsonProcessingException {
    String body = "404 page not found";
    String rbody = netlasWrapper.getBody();
    int statusCode = 404;
    int rstatusCode = netlasWrapper.getStatusCode();

    boolean hasServerHeader = true;
    try {
      List<String> servers = netlasWrapper.getServers();
      hasServerHeader = !servers.isEmpty();
    } catch (Exception e) {
      hasServerHeader = false;
    }

    return new Response(rbody.contains(body) && rstatusCode == statusCode && !hasServerHeader);
  }
}
