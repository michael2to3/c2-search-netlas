package c2.search.netlas.comparator;

import java.util.Comparator;
import java.util.List;
import netlas.java.scheme.Subject;

public class SubjectComparator implements Comparator<Subject> {

  @Override
  public int compare(Subject subject1, Subject subject2) {
    int result;

    result = compareLists(subject1.getCountry(), subject2.getCountry());
    if (result != 0) return result;

    result = compareLists(subject1.getOrganization(), subject2.getOrganization());
    if (result != 0) return result;

    result = compareLists(subject1.getCommonName(), subject2.getCommonName());
    if (result != 0) return result;

    result = compareLists(subject1.getProvince(), subject2.getProvince());
    if (result != 0) return result;

    result = compareLists(subject1.getLocality(), subject2.getLocality());
    if (result != 0) return result;

    result = compareLists(subject1.getOrganizationalUnit(), subject2.getOrganizationalUnit());

    return result;
  }

  private int compareLists(List<String> lhs, List<String> rhs) {
    int size1 = lhs.size();
    int size2 = rhs.size();

    int minSize = Math.min(size1, size2);

    for (int i = 0; i < minSize; i++) {
      int comparison = lhs.get(i).compareToIgnoreCase(rhs.get(i));
      if (comparison != 0) {
        return comparison;
      }
    }

    return Integer.compare(size1, size2);
  }

  public boolean equals(Subject lhs, Subject rhs) {
    return compare(lhs, rhs) == 0;
  }

  public int hashCode(Subject struct) {
    return struct.hashCode();
  }
}
