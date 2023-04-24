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

      if (!baseHeaders.containsKey(key)) {
        if (targetValues == null || targetValues.isEmpty()) {
          continue;
        }
        return -1;
      }

      List<String> baseValues = baseHeaders.get(key);
      if (baseValues == null || baseValues.isEmpty()) {
        if (targetValues == null || targetValues.isEmpty()) {
          continue;
        }
        return -1;
      }

      boolean hasCommonElement = false;
      for (String targetValue : targetValues) {
        if (baseValues.contains(targetValue)) {
          hasCommonElement = true;
          break;
        }
      }

      if (!hasCommonElement) {
        return -1;
      }
    }

    return 0;
  }
}
