package c2.search.netlas.broadcast;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
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
      String startIP = matcher.group(1);
      String endIP = matcher.group(2);

      try {
        InetAddress startAddress = InetAddress.getByName(startIP);
        InetAddress endAddress = InetAddress.getByName(endIP);

        ByteBuffer startAddressBuffer = ByteBuffer.wrap(startAddress.getAddress());
        ByteBuffer endAddressBuffer = ByteBuffer.wrap(endAddress.getAddress());

        if (startAddressBuffer.compareTo(endAddressBuffer) <= 0) {
          normalizedRange = "[" + startIP + " TO " + endIP + "]";
        } else {
          throw new IpRangeFormatException("Invalid IP range");
        }
      } catch (UnknownHostException e) {
        throw new IpRangeFormatException("Invalid IP range format");
      }
    } else {
      throw new IpRangeFormatException("Invalid IP range format");
    }

    return normalizedRange;
  }
}
