package c2.search.netlas;

import java.util.HashMap;
import java.util.Map;
import netlas.java.Netlas;
import netlas.java.exception.NetlasRequestException;
import netlas.java.scheme.Response;

public final class NetlasCache {
  private static NetlasCache instance;
  private final Netlas netlas;
  private final Map<String, Object> cache;

  private NetlasCache(final String apiKey) {
    this.netlas = Netlas.newBuilder().setApiKey(apiKey).build();
    this.cache = new HashMap<>();
  }

  public static NetlasCache getInstance() {
    return instance;
  }

  public static NetlasCache getInstance(final String apiKey) {
    if (instance == null) {
      instance = new NetlasCache(apiKey);
    }
    return instance;
  }

  public static Netlas getNetlas() {
    return instance.netlas;
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
      response = fetchResponse(query, page, indices, fields, excludeFields);
      cache.put(cacheKey, response);
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
}
