package c2.search.netlas.query;

import java.util.List;
import java.util.StringJoiner;
import netlas.java.scheme.Issuer;

public class IssuerBuilder extends QueryBuilder {
  private final StringJoiner joiner;

  public IssuerBuilder(final Issuer issuer) {
    super();
    this.joiner = generate(issuer);
  }

  @Override
  public String build() {
    return joiner.toString();
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
      joiner.add(builderItem.build());
    }
    return joiner;
  }
}
