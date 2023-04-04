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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Host host = (Host) o;
    return port == host.port && target.equals(host.target);
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
