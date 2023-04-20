package c2.search.netlas.query;

import java.util.List;
import java.util.StringJoiner;

public class ListBuilder extends QueryBuilder {
  private final StringJoiner joiner;

  public ListBuilder(final String value, final List<String> list) {
    super();
    this.joiner = generate(value, list);
  }

  @Override
  public String build() {
    return joiner.toString();
  }

  private StringJoiner generate(final String value, final List<String> list) {
    StringJoiner joiner = new StringJoiner(getSeparator());
    if (list == null) {
      return joiner;
    }
    for (String c : list) {
      joiner.add(String.format("%s:\"%s\"", value, c));
    }
    return joiner;
  }
}
