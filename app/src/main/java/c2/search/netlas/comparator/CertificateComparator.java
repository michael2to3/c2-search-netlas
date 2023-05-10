package c2.search.netlas.comparator;

import java.util.Comparator;
import netlas.java.scheme.Certificate;

public class CertificateComparator implements Comparator<Certificate> {
  public CertificateComparator() {}

  @Override
  public int compare(final Certificate base, final Certificate target) {
    final SubjectComparator subjectComparator = new SubjectComparator();
    final IssuerComparator issuerComparator = new IssuerComparator();
    return subjectComparator.compare(base.getSubject(), target.getSubject())
        + issuerComparator.compare(base.getIssuer(), target.getIssuer());
  }
}
