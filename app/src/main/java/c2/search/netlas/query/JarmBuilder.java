package c2.search.netlas.query;

import java.util.List;

public class JarmBuilder implements QueryBuilder {
  private final List<String> body;
  private String separator;

  public JarmBuilder(final List<String> body) {
    this.body = body;
    this.separator = " OR ";
  }

  @Override
  public String build() {
    QueryBuilder builder = new ListBuilder("jarm", body);
    builder.setSeparator(separator);
    return builder.build();
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
