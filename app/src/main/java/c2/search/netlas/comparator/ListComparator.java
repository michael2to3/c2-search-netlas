package c2.search.netlas.comparator;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListComparator implements Comparator<List<String>> {

  @Override
  public int compare(List<String> base, List<String> target) {
    if(base == null || target == null) {
      return -1;
    }
    Set<String> baseSet = new HashSet<>(base);
    for (String element : target) {
      if (baseSet.contains(element)) {
        return 0;
      }
    }
    return -1;
  }
}
