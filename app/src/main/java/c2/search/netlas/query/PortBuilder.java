package c2.search.netlas.query;

import java.util.List;

public class PortBuilder implements QueryBuilder {
  private final List<Integer> body;
  private String separator;

  public PortBuilder(final List<Integer> body) {
    this.body = body;
    this.separator = " OR ";
  }

  @Override
  public String build() {
    final QueryBuilder builder =
        new ListBuilder("port", body.stream().map(Object::toString).toList());
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
