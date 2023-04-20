package c2.search.netlas.query;

import java.util.List;

public class HeaderBuilder extends QueryBuilder {
  public HeaderBuilder(final List<String> body) {
    super();
  }

  @Override
  public String build() {
    // TODO how to check exists header in netlas
    return "";
  }
}
