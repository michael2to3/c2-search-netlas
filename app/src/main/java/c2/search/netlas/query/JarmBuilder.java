package c2.search.netlas.query;

import java.util.List;

public class JarmBuilder extends QueryBuilder {
  private final List<String> body;

  public JarmBuilder(final List<String> body) {
    super();
    this.body = body;
  }

  @Override
  public String build() {
    return new ListBuilder("jarm", body).build();
  }
}
