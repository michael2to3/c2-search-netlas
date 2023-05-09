package c2.search.netlas.target.metasploit;

import c2.search.netlas.analyze.StaticData;
import c2.search.netlas.annotation.Static;
import java.util.ArrayList;
import java.util.List;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;

@Static(name = "Metasploit")
public class MetasploitStatic implements StaticData {
  public MetasploitStatic() {}

  @Override
  public List<String> getJarm() {
    final List<String> jarmv5 =
        List.of("07d14d16d21d21d07c42d43d000000f50d155305214cf247147c43c0f1a823");
    final List<String> jarmv6 =
        List.of(
            "07d19d12d21d21d07c42d43d000000f50d155305214cf247147c43c0f1a823",
            "07b03b12b21b21b07b07b03b07b21b23aeefb38b723c523befb314af6e95ac",
            "07c03c12c21c21c07c07c03c07c21c23aeefb38b723c523befb314af6e95ac",
            "07d19d12d21d21d00007d19d07d21d0ae59125bcd90b8876b50928af8f6cd4");

    final List<String> total = new ArrayList<>();
    total.addAll(jarmv5);
    total.addAll(jarmv6);

    return total;
  }

  @Override
  public List<Certificate> getCertificate() {
    return List.of();
  }

  @Override
  public List<Integer> getPort() {
    return List.of();
  }

  @Override
  public List<Headers> getHeader() {
    final Headers headers = new Headers();
    headers.setHeader("server", List.of("apache"));
    return List.of(headers);
  }

  @Override
  public List<String> getBodyAsSha256() {
    final var body = "534c3073bb1d373b2c76f9c85cc7373658a7dbbe7cc7eccdf5af751ed91951df";
    return List.of(body);
  }
}
