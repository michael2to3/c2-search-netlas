package c2.search.netlas.comparator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import netlas.java.scheme.Subject;

public class SubjectComparator implements Comparator<Subject> {
  public SubjectComparator() {}

  @Override
  public int compare(final Subject base, final Subject target) {
    final ListComparator comp = new ListComparator();

    final List<Supplier<Integer>> comparisons =
        Arrays.asList(
            () -> comp.compare(base.getCountry(), target.getCountry()),
            () -> comp.compare(base.getOrganization(), target.getOrganization()),
            () -> comp.compare(base.getCommonName(), target.getCommonName()),
            () -> comp.compare(base.getProvince(), target.getProvince()),
            () -> comp.compare(base.getLocality(), target.getLocality()),
            () -> comp.compare(base.getOrganizationalUnit(), target.getOrganizationalUnit()));

    return comparisons.stream()
        .map(Supplier::get)
        .filter(result -> result != 0)
        .findFirst()
        .orElse(0);
  }
}
