package c2.search.netlas.query;

import java.util.List;

public class BodyAsSha256Builder implements QueryBuilder {
  private final List<String> body;
  private String separator;

  public BodyAsSha256Builder(final List<String> body) {
    this.body = body;
    this.separator = " OR ";
  }

  @Override
  public String build() {
    final QueryBuilder builder = new ListBuilder("http.body_sha256", body);
    builder.setSeparator(separator);
    return builder.build();
  }

  @Override
  public void setSeparator(final String separator) {
    this.separator = separator;
  }

  @Override
  public String getSeparator() {
    return separator;
  }
}
