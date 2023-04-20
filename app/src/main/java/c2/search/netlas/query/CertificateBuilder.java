package c2.search.netlas.query;

import java.util.StringJoiner;
import netlas.java.scheme.Certificate;

public class CertificateBuilder extends QueryBuilder {
  private final StringJoiner joiner;

  public CertificateBuilder(final Certificate certificate) {
    super();
    this.joiner = generate(certificate);
  }

  @Override
  public String build() {
    return joiner.toString();
  }

  private StringJoiner generate(final Certificate certificate) {
    StringJoiner joiner = new StringJoiner(getSeparator());
    if (certificate == null) {
      return joiner;
    }
    QueryBuilder subject = new SubjectBuilder(certificate.getSubject());
    QueryBuilder issuer = new IssuerBuilder(certificate.getIssuer());
    joiner.add(subject.build());
    joiner.add(issuer.build());
    return joiner;
  }
}
