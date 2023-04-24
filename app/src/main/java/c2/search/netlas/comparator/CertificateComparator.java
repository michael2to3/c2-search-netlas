package c2.search.netlas.comparator;

import java.util.Comparator;
import netlas.java.scheme.Certificate;

public class CertificateComparator implements Comparator<Certificate> {

  @Override
  public int compare(Certificate base, Certificate target) {
    SubjectComparator subjectComparator = new SubjectComparator();
    IssuerComparator issuerComparator = new IssuerComparator();
    return subjectComparator.compare(base.getSubject(), target.getSubject())
        + issuerComparator.compare(base.getIssuer(), target.getIssuer());
  }
}
