package c2.search.netlas.comparator;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListComparator implements Comparator<List<String>> {
  public ListComparator() {}

  @Override
  public int compare(final List<String> base, final List<String> target) {
    if (base == null || target == null) {
      return -1;
    }
    final Set<String> baseSet = new HashSet<>(base);
    for (final String element : target) {
      if (baseSet.contains(element)) {
        return 0;
      }
    }
    return -1;
  }
}
