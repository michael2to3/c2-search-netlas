package c2.search.netlas.broadcast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NormalizeRangeHost {
  private final String range;

  public NormalizeRangeHost(final String range) {
    this.range = range;
  }

  public String normalize() throws IpRangeFormatException {
    String normalizedRange = null;
    Pattern pattern =
        Pattern.compile(
            "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\s*[-TO,]\\s*(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})");
    Matcher matcher = pattern.matcher(range);

    if (matcher.find()) {
      normalizedRange = "[" + matcher.group(1) + " TO " + matcher.group(2) + "]";
    } else {
      throw new IpRangeFormatException("Invalid IP range format");
    }

    return normalizedRange;
  }
}
