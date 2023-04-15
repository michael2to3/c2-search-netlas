package c2.search.netlas.scheme;

import java.util.Objects;

public class Host {
  private String target;
  private int port;
  private String path;

  private Host() {
    this.path = "/";
  }

  public Host(final String target, final int port) {
    this.target = target;
    this.port = port;
    this.path = "/";
  }

  @Override
  public boolean equals(final Object obj) {
    boolean result;
    if (this == obj) {
      result = true;
    } else if (obj == null || getClass() != obj.getClass()) {
      result = false;
    } else {
      final Host host = (Host) obj;
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

  public String getPath() {
    return path;
  }

  public void setPath(final String path) {
    this.path = path;
  }

  public static Builder newBuilder() {
    return new Host().new Builder();
  }

  public class Builder {
    private Builder() {}

    public Builder setTarget(final String target) {
      Host.this.target = target;
      return this;
    }

    public Builder setPort(final int port) {
      Host.this.port = port;
      return this;
    }

    public Builder setPath(final String path) {
      Host.this.path = path;
      return this;
    }

    public Host build() {
      return Host.this;
    }
  }
}
