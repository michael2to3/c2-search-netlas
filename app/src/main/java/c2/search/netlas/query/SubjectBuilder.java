package c2.search.netlas.query;

import java.util.List;
import java.util.StringJoiner;
import netlas.java.scheme.Subject;

public class SubjectBuilder implements QueryBuilder {
  private final Subject subject;
  private String separator;

  public SubjectBuilder(final Subject subject) {
    this.subject = subject;
    this.separator = " AND ";
  }

  @Override
  public String build() {
    return generate(subject).toString();
  }

  @Override
  public void setSeparator(String separator) {
    this.separator = separator;
  }

  @Override
  public String getSeparator() {
    return separator;
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
      builderItem.setSeparator(" OR ");
      String query = builderItem.build();
      if (query != null && !query.isEmpty()) {
        joiner.add(query);
      }
    }
    return joiner;
  }
}
