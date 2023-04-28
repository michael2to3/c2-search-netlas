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
    Pattern subnetPattern =
        Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})/(\\d{1,2})");
    Matcher matcher = pattern.matcher(range);
    Matcher subnetMatcher = subnetPattern.matcher(range);

    if (matcher.find()) {
      String startIP = matcher.group(1);
      String endIP = matcher.group(2);

      if (!isValidIPAddress(startIP) || !isValidIPAddress(endIP)) {
        throw new IpRangeFormatException("Invalid IP range format");
      }

      normalizedRange = processIpRange(matcher);
    } else if (subnetMatcher.find()) {
      subnetPattern = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\/(\\d{1,2})");
      subnetMatcher = subnetPattern.matcher(range);

      if (subnetMatcher.find()) {
        String ipAddress = subnetMatcher.group(1);
        String subnetMask = subnetMatcher.group(2);

        if (isValidIPAddress(ipAddress) && isValidSubnetMask(subnetMask)) {
          return "\"" + ipAddress + "/" + subnetMask + "\"";
        } else {
          throw new IpRangeFormatException("Invalid IP range format");
        }
      } else {
        throw new IpRangeFormatException("Invalid IP range format");
      }
    } else {
      throw new IpRangeFormatException("Invalid IP range format");
    }

    return normalizedRange;
  }

  private String processIpRange(Matcher matcher) throws IpRangeFormatException {
    String startIP = matcher.group(1);
    String endIP = matcher.group(2);

    try {
      InetAddress startAddress = InetAddress.getByName(startIP);
      InetAddress endAddress = InetAddress.getByName(endIP);

      ByteBuffer startAddressBuffer = ByteBuffer.wrap(startAddress.getAddress());
      ByteBuffer endAddressBuffer = ByteBuffer.wrap(endAddress.getAddress());

      if (startAddressBuffer.compareTo(endAddressBuffer) <= 0) {
        return "[" + startIP + " TO " + endIP + "]";
      } else {
        throw new IpRangeFormatException("Invalid IP range");
      }
    } catch (UnknownHostException e) {
      throw new IpRangeFormatException("Invalid IP range format");
    }
  }

  private boolean isValidIPAddress(String ipAddress) {
    String[] octets = ipAddress.split("\\.");

    if (octets.length != 4) {
      return false;
    }

    for (String octet : octets) {
      try {
        int num = Integer.parseInt(octet);

        if (num < 0 || num > 255) {
          return false;
        }
      } catch (NumberFormatException e) {
        return false;
      }
    }

    return true;
  }

  private boolean isValidSubnetMask(String subnetMask) {
    try {
      int mask = Integer.parseInt(subnetMask);
      return mask >= 0 && mask <= 32;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
