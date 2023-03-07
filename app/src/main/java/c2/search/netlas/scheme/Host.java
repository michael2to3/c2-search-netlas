package c2.search.netlas.scheme;

public class Host {
  private String host;
  private int port;

  public Host() {
  }

  public Host(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }
}
