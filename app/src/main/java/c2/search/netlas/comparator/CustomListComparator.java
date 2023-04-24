package c2.search.netlas.comparator;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;

public class CustomListComparator<T, U> implements Comparator<List<T>> {

  private final BiPredicate<T, U> customComparator;
  private final List<U> target;

  public CustomListComparator(BiPredicate<T, U> customComparator, List<U> target) {
    this.customComparator = customComparator;
    this.target = target;
  }

  @Override
  public int compare(List<T> base, List<T> otherBase) {
    for (T baseElement : base) {
      for (U targetElement : target) {
        if (customComparator.test(baseElement, targetElement)) {
          return 0;
        }
      }
    }
    return -1;
  }
}
