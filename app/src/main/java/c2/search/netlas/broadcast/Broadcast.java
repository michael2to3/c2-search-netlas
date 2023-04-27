package c2.search.netlas.broadcast;

import c2.search.netlas.analyze.StaticData;
import java.util.List;
import netlas.java.Netlas;
import netlas.java.exception.NetlasRequestException;
import netlas.java.scheme.Response;

public class Broadcast {
  private final Netlas netlas;
  private final String range;
  private final List<StaticData> data;

  public Broadcast(Netlas netlas, String range, List<StaticData> data)
      throws IpRangeFormatException {
    this.netlas = netlas;
    this.range = new NormalizeRangeHost(range).normalize();
    this.data = data;
  }

  public Response execute() throws NetlasRequestException {
    String query = GenerateQuery.generate(range, data);
    return netlas.response(query, 0, null, null, false);
  }
}
