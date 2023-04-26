package c2.search.netlas.query;

import java.util.List;
import java.util.StringJoiner;

public class ListBuilder implements QueryBuilder {
  private final String value;
  private List<String> list;
  private String separator;

  public ListBuilder(final String value, final List<String> list) {
    this.value = value;
    this.list = list;
    this.separator = " OR ";
  }

  @Override
  public String build() {
    String query = generate(value, list).toString();
    if (list == null) {
      return "";
    }
    if (list.size() > 1) {
      return String.format("(%s)", query);
    }
    return query;
  }

  @Override
  public void setSeparator(String separator) {
    this.separator = separator;
  }

  @Override
  public String getSeparator() {
    return separator;
  }

  private StringJoiner generate(final String value, final List<String> list) {
    StringJoiner joiner = new StringJoiner(getSeparator());
    if (list == null || list.isEmpty()) {
      return joiner;
    }
    for (String c : list) {
      if (c == null) {
        joiner.add(String.format("%s:*", value));
      } else {
        joiner.add(String.format("%s:\"%s\"", value, c));
      }
    }
    return joiner;
  }
}
