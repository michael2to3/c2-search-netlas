package c2.search.netlas.query;

import java.util.List;
import java.util.StringJoiner;
import netlas.java.scheme.Issuer;

public class IssuerBuilder implements QueryBuilder {
  private final Issuer issuer;
  private String separator;

  public IssuerBuilder(final Issuer issuer) {
    this.issuer = issuer;
    this.separator = " AND ";
  }

  @Override
  public String build() {
    return generate(issuer).toString();
  }

  @Override
  public void setSeparator(String separator) {
    this.separator = separator;
  }

  @Override
  public String getSeparator() {
    return separator;
  }

  private StringJoiner generate(final Issuer issuer) {
    StringJoiner joiner = new StringJoiner(getSeparator());
    if (issuer == null) {
      return joiner;
    }
    List<QueryBuilder> builder =
        List.of(
            new ListBuilder("certificate.issuer.country", issuer.getCountry()),
            new ListBuilder("certificate.issuer.locality", issuer.getLocality()),
            new ListBuilder("certificate.issuer.organization", issuer.getOrganization()),
            new ListBuilder(
                "certificate.issuer.organizational_unit", issuer.getOrganizationalUnit()),
            new ListBuilder("certificate.issuer.common_name", issuer.getCommonName()),
            new ListBuilder("certificate.issuer.province", issuer.getProvince()));

    for (QueryBuilder builderItem : builder) {
      builderItem.setSeparator(separator);
      String query = builderItem.build();
      if (query != null && !query.isEmpty()) {
        joiner.add(query);
      }
    }
    return joiner;
  }
}
