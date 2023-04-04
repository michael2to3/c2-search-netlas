package c2.search.netlas.scheme;

import java.util.Objects;

public class Host {
  private String target;
  private int port;

  public Host() {}

  public Host(final String target, final int port) {
    this.target = target;
    this.port = port;
  }

  @Override
  public boolean equals(final Object o) {
    boolean result;
    if (this == o) {
      result = true;
    } else if (o == null || getClass() != o.getClass()) {
      result = false;
    } else {
      final Host host = (Host) o;
      result = port == host.port && target.equals(host.target);
    }
    return result;
  }

  @Override
  public int hashCode() {
    return Objects.hash(target, port);
  }

  @Override
  public String toString() {
    return String.format("%s:%d", target, port);
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(final String target) {
    this.target = target;
  }

  public int getPort() {
    return port;
  }

  public void setPort(final int port) {
    this.port = port;
  }
}
