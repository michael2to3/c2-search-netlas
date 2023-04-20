package c2.search.netlas.query;

import java.util.List;

public class PortBuilder extends QueryBuilder {
  private final List<String> body;

  public PortBuilder(final List<String> body) {
    super();
    this.body = body;
  }

  @Override
  public String build() {
    return new ListBuilder("port", body).build();
  }
}
