package c2.search.netlas.broadcast;

public class IpRangeFormatException extends Exception {
  private static final long serialVersionUID = 1L;

  public IpRangeFormatException(final String message) {
    super(message);
  }

  public IpRangeFormatException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public IpRangeFormatException(final String format, final Object... args) {
    super(String.format(format, args));
  }

  public IpRangeFormatException(final Throwable cause, final String format, final Object... args) {
    super(String.format(format, args), cause);
  }
}
