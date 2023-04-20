package c2.search.netlas.query;

import java.util.List;
import java.util.StringJoiner;
import netlas.java.scheme.Subject;

public class SubjectBuilder extends QueryBuilder {
  private final StringJoiner joiner;

  public SubjectBuilder(final Subject subject) {
    super();
    this.joiner = generate(subject);
  }

  @Override
  public String build() {
    return joiner.toString();
  }

  private StringJoiner generate(final Subject subject) {
    StringJoiner joiner = new StringJoiner(getSeparator());
    if (subject == null) {
      return joiner;
    }
    List<QueryBuilder> builder =
        List.of(
            new ListBuilder("certificate.subject.country", subject.getCountry()),
            new ListBuilder("certificate.subject.locality", subject.getLocality()),
            new ListBuilder("certificate.subject.organization", subject.getOrganization()),
            new ListBuilder(
                "certificate.subject.organizational_unit", subject.getOrganizationalUnit()),
            new ListBuilder("certificate.subject.common_name", subject.getCommonName()),
            new ListBuilder("certificate.subject.province", subject.getProvince()));

    for (QueryBuilder builderItem : builder) {
      joiner.add(builderItem.build());
    }
    return joiner;
  }
}
