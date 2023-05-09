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
    String normalizedRange;
    final Matcher matcher = getIpRangeMatcher();
    final Matcher subnetMatcher = getSubnetMatcher();

    if (matcher.find()) {
      normalizedRange = processIpRange(matcher);
    } else if (subnetMatcher.find()) {
      normalizedRange = processSubnet(subnetMatcher);
    } else {
      throw new IpRangeFormatException("Invalid IP range format: " + range);
    }

    return normalizedRange;
  }

  private String processIpRange(final Matcher matcher) throws IpRangeFormatException {
    final String startIP = matcher.group(1);
    final String endIP = matcher.group(2);

    if (isValidIPAddress(startIP) && isValidIPAddress(endIP)) {
      return formatIpRange(startIP, endIP);
    } else if (!isValidIPAddress(startIP) && !isValidIPAddress(endIP)) {
      throw new IpRangeFormatException("Invalid IP address format in start and end IPs: " + range);
    } else if (!isValidIPAddress(startIP)) {
      throw new IpRangeFormatException("Invalid IP address format in start IP: " + range);
    } else {
      throw new IpRangeFormatException("Invalid IP address format in end IP: " + range);
    }
  }

  private String processSubnet(final Matcher subnetMatcher) throws IpRangeFormatException {
    final String ipAddress = subnetMatcher.group(1);
    final String subnetMask = subnetMatcher.group(2);

    if (isValidIPAddress(ipAddress) && isValidSubnetMask(subnetMask)) {
      return "\"" + ipAddress + "/" + subnetMask + "\"";
    } else if (!isValidIPAddress(ipAddress)) {
      throw new IpRangeFormatException("Invalid IP address format in subnet IP: " + range);
    } else {
      throw new IpRangeFormatException("Invalid subnet mask format in subnet IP: " + range);
    }
  }

  private Matcher getIpRangeMatcher() {
    final Pattern pattern =
        Pattern.compile(
            "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\s*[-TO,]\\s*(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})");
    return pattern.matcher(range);
  }

  private Matcher getSubnetMatcher() {
    final Pattern subnetPattern =
        Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})/(\\d{1,2})");
    return subnetPattern.matcher(range);
  }

  private String formatIpRange(final String startIP, final String endIP)
      throws IpRangeFormatException {
    try {
      final InetAddress startAddress = InetAddress.getByName(startIP);
      final InetAddress endAddress = InetAddress.getByName(endIP);

      final ByteBuffer startBuffer = ByteBuffer.wrap(startAddress.getAddress());
      final ByteBuffer endBuffer = ByteBuffer.wrap(endAddress.getAddress());

      if (startBuffer.compareTo(endBuffer) > 0) {
        throw new IpRangeFormatException(
            "Invalid IP range format: start IP must be less than or equal to end IP: " + range);
      }

      return "[" + startIP + " TO " + endIP + "]";
    } catch (UnknownHostException e) {
      throw new IpRangeFormatException(
          "Invalid IP range format: invalid start or end IP address: " + range, e);
    }
  }

  private boolean isValidIPAddress(final String ipAddress) {
    final int len = 4;
    final String[] octets = ipAddress.split("\\.");

    if (octets.length != len) {
      return false;
    }

    return isAllOctetsValid(octets);
  }

  private boolean isAllOctetsValid(final String[] octets) {
    final int min = 0;
    final int max = 255;
    for (final String octet : octets) {
      try {
        final int num = Integer.parseInt(octet);

        if (num < min || num > max) {
          return false;
        }

      } catch (NumberFormatException e) {
        return false;
      }
    }

    return true;
  }

  private boolean isValidSubnetMask(final String subnetMask) {
    final int min = 0;
    final int max = 32;
    try {
      final int mask = Integer.parseInt(subnetMask);
      return mask >= min && mask <= max;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
