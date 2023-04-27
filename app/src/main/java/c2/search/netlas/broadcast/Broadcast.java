package c2.search.netlas.broadcast;

import c2.search.netlas.analyze.StaticData;
import java.util.List;
import netlas.java.Netlas;
import netlas.java.exception.NetlasRequestException;
import netlas.java.scheme.Response;

public class Broadcast {
  private final Netlas netlas;
  private final String query;

  public Broadcast(Netlas netlas, String range, List<StaticData> data) throws IpRangeFormatException {
    this.netlas = netlas;
    String nrange = new NormalizeRangeHost(range).normalize();
    this.query = GenerateQuery.generate(nrange, data);
  }

  public Response execute() throws NetlasRequestException {
    return netlas.response(query, 0, null, null, false);
  }
}
