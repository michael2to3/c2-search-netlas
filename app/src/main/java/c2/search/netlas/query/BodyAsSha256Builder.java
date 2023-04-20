package c2.search.netlas.query;

import java.util.List;

public class BodyAsSha256Builder extends QueryBuilder {
  private final List<String> body;

  public BodyAsSha256Builder(final List<String> body) {
    super();
    this.body = body;
  }

  @Override
  public String build() {
    return new ListBuilder("http.body_sha256", body).build();
  }
}
