package c2.search.netlas.broadcast;

import c2.search.netlas.analyze.StaticData;
import c2.search.netlas.query.StaticBuilder;
import java.util.List;
import java.util.StringJoiner;

class GenerateQuery {
  public static String generate(String range, List<StaticData> data) {
    StringJoiner joiner = new StringJoiner(" OR ");
    for (StaticData staticData : data) {
      StaticBuilder builder = new StaticBuilder(staticData);
      joiner.add(builder.build());
    }
    return String.format("[%s] AND (%s)", range, joiner.toString());
  }
}
