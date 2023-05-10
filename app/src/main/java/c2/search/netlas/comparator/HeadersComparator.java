package c2.search.netlas.comparator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import netlas.java.scheme.Headers;

public class HeadersComparator implements Comparator<Headers> {
  public HeadersComparator() {}

  @Override
  public int compare(final Headers base, final Headers target) {
    final Map<String, List<String>> baseHeaders = base.getHeaders();
    final Map<String, List<String>> targetHeaders = target.getHeaders();

    for (final Map.Entry<String, List<String>> entry : targetHeaders.entrySet()) {
      final String key = entry.getKey();
      final List<String> targetValues = entry.getValue();

      if (isNullOrEmpty(baseHeaders.get(key))) {
        if (isNullOrEmpty(targetValues)) {
          continue;
        }
        return -1;
      }

      final List<String> baseValues = baseHeaders.get(key);
      if (isNullOrEmpty(baseValues)) {
        if (isNullOrEmpty(targetValues)) {
          continue;
        }
        return -1;
      }

      if (targetValues.stream().noneMatch(baseValues::contains)) {
        return -1;
      }
    }

    return 0;
  }

  private boolean isNullOrEmpty(final List<String> list) {
    return list == null || list.isEmpty();
  }
}
