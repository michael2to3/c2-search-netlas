package c2.search.netlas;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import netlas.java.Netlas;
import netlas.java.exception.NetlasRequestException;
import netlas.java.scheme.Response;

public final class NetlasCache {
  private final Netlas netlas;
  private final Map<String, Object> cache;

  private NetlasCache(final String apiKey) {
    this.netlas = Netlas.newBuilder().setApiKey(apiKey).build();
    this.cache = new ConcurrentHashMap<>();
  }

  public static NetlasCache getInstance() {
    return Holder.instance;
  }

  public static void changeApiKey(final String apiKey) {
    synchronized (Holder.class) {
      if (Holder.instance == null) {
        Holder.instance = new NetlasCache(apiKey);
      }
    }
  }

  public static Netlas getNetlas() {
    return getInstance().netlas;
  }

  public Response response(
      final String query,
      final int page,
      final String indices,
      final String fields,
      final boolean excludeFields)
      throws NetlasRequestException {
    final String cacheKey = buildCacheKey("response", query, page, indices, fields, excludeFields);
    var response = (Response) cache.get(cacheKey);
    if (response == null) {
      synchronized (cache) {
        response = (Response) cache.get(cacheKey);
        if (response == null) {
          response = fetchResponse(query, page, indices, fields, excludeFields);
          cache.put(cacheKey, response);
        }
      }
    }
    return response;
  }

  private Response fetchResponse(
      final String query,
      final int page,
      final String indices,
      final String fields,
      final boolean excludeFields)
      throws NetlasRequestException {
    return netlas.response(query, page, indices, fields, excludeFields);
  }

  private String buildCacheKey(
      final String method,
      final String query,
      final int page,
      final String indices,
      final String fields,
      final boolean excludeFields) {
    return method + "|" + query + "|" + page + "|" + indices + "|" + fields + "|" + excludeFields;
  }

  private static class Holder {
    private static NetlasCache instance;

    static {
      final String apiKey = System.getenv("API_KEY");
      if (apiKey != null && !apiKey.isEmpty()) {
        instance = new NetlasCache(apiKey);
      }
    }
  }
}
