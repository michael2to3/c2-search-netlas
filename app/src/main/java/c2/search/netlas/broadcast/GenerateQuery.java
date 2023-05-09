package c2.search.netlas.broadcast;

import c2.search.netlas.analyze.StaticData;
import c2.search.netlas.query.StaticBuilder;
import java.util.List;
import java.util.StringJoiner;

public class GenerateQuery {
  public static String generate(final String range, final List<StaticData> data) {
    final StringJoiner joiner = new StringJoiner(" OR ");
    for (final StaticData staticData : data) {
      final StaticBuilder builder = new StaticBuilder(staticData);
      joiner.add(builder.build());
    }
    return String.format("[%s] AND (%s)", range, joiner.toString());
  }
}
