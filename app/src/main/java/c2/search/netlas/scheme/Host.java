package c2.search.netlas.scheme;

import java.util.Objects;

public class Host {
  private String target;
  private int port;

  public Host() {}

  public Host(String target, int port) {
    this.target = target;
    this.port = port;
  }

  @Override
  public boolean equals(Object obj) {
    boolean result;
    if (this == obj) {
      return true;
    } else if (obj == null || getClass() != obj.getClass()) {
      return false;
    } else {
      Host host = (Host) obj;
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

  public void setTarget(String target) {
    this.target = target;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }
}
