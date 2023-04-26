package c2.search.netlas.query;

import java.util.StringJoiner;
import netlas.java.scheme.Certificate;

public class CertificateBuilder implements QueryBuilder {
  private final Certificate certificate;
  private String separator;

  public CertificateBuilder(final Certificate certificate) {
    this.certificate = certificate;
    this.separator = " AND ";
  }

  @Override
  public String build() {
    String query = generate(certificate).toString();
    if (query.isEmpty()) {
      return "";
    }
    return String.format("(%s)", query);
  }

  private StringJoiner generate(final Certificate certificate) {
    StringJoiner joiner = new StringJoiner(getSeparator());
    if (certificate == null) {
      return joiner;
    }
    QueryBuilder subject = new SubjectBuilder(certificate.getSubject());
    QueryBuilder issuer = new IssuerBuilder(certificate.getIssuer());
    subject.setSeparator(separator);
    issuer.setSeparator(separator);
    String subjectQuery = subject.build();
    String issuerQuery = issuer.build();
    if (subjectQuery != null && !subjectQuery.isEmpty()) {
      joiner.add(subject.build());
    }
    if (issuerQuery != null && !issuerQuery.isEmpty()) {
      joiner.add(issuer.build());
    }
    return joiner;
  }

  @Override
  public void setSeparator(String separator) {
    this.separator = separator;
  }

  @Override
  public String getSeparator() {
    return separator;
  }
}
