package c2.search.netlas.comparator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import netlas.java.scheme.Issuer;

public class IssuerComparator implements Comparator<Issuer> {
  @Override
  public int compare(Issuer base, Issuer target) {
    if (base == null || target == null) {
      return 0;
    }
    ListComparator comp = new ListComparator();

    List<Supplier<Integer>> comparisons =
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
