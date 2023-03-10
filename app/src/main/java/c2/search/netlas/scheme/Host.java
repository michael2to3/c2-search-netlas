package c2.search.netlas.scheme;

public class Host {
  private String target;
  private int port;

  public Host() {}

  public Host(String target, int port) {
    this.target = target;
    this.port = port;
  }

  @Override public String toString() {
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
