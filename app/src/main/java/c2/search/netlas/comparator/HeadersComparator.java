package c2.search.netlas.comparator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import netlas.java.scheme.Headers;

public class HeadersComparator implements Comparator<Headers> {

  @Override
  public int compare(Headers base, Headers target) {
    Map<String, List<String>> baseHeaders = base.getHeaders();
    Map<String, List<String>> targetHeaders = target.getHeaders();

    for (Map.Entry<String, List<String>> entry : targetHeaders.entrySet()) {
      String key = entry.getKey();
      List<String> targetValues = entry.getValue();

      if (isNullOrEmpty(baseHeaders.get(key))) {
        if (isNullOrEmpty(targetValues)) {
          continue;
        }
        return -1;
      }

      List<String> baseValues = baseHeaders.get(key);
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

  private boolean isNullOrEmpty(List<String> list) {
    return list == null || list.isEmpty();
  }
}
