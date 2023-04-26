package c2.search.netlas.query;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import netlas.java.scheme.Headers;

public class HeaderBuilder implements QueryBuilder {
  private final Headers body;
  private String separator;

  public HeaderBuilder(final Headers body) {
    this.body = body;
    this.separator = " AND ";
  }

  @Override
  public String build() {
    StringJoiner joiner = new StringJoiner(separator);
    for (Map.Entry<String, List<String>> entry : body.getHeaders().entrySet()) {

      String query = String.format("http.headers.%s:*", entry.getKey());
      if (entry.getValue() != null) {
        String key = String.format("http.headers.%s", entry.getKey());
        QueryBuilder valueBuilder = new ListBuilder(key, entry.getValue());
        valueBuilder.setSeparator(" OR ");
        query = valueBuilder.build();
      }
      joiner.add(query);
    }
    String total = joiner.toString();
    if (!total.isEmpty()) {
      total = String.format("(%s)", total);
    }
    return total;
  }

  @Override
  public void setSeparator(String separator) {
    this.separator = separator;
  }

  @Override
  public String getSeparator() {
    return separator;
  }
}
