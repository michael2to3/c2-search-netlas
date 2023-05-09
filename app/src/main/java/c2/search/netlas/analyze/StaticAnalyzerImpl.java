package c2.search.netlas.analyze;

import c2.search.netlas.annotation.Static;
import c2.search.netlas.comparator.CertificateComparator;
import c2.search.netlas.comparator.HeadersComparator;
import c2.search.netlas.scheme.Results;
import java.util.List;
import java.util.Map;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Data;
import netlas.java.scheme.Headers;

public class StaticAnalyzerImpl implements StaticAnalyzer {
  private final Data response;

  public StaticAnalyzerImpl(final Data response) {
    this.response = response;
  }

  private c2.search.netlas.scheme.Response passJarm(final StaticData data) {
    final String baseJarms = response.getJarm();
    if (data.getJarm() == null || baseJarms == null) {
      return c2.search.netlas.scheme.Response.newBuilder()
          .setSuccess(false)
          .setDescription("Jarm is null")
          .build();
    }
    final boolean pass = data.getJarm().contains(baseJarms);
    return c2.search.netlas.scheme.Response.newBuilder()
        .setSuccess(pass)
        .setDescription("Jarm matched")
        .build();
  }

  private c2.search.netlas.scheme.Response passCert(final StaticData data) {
    final List<Certificate> target = data.getCertificate();
    if (data.getCertificate() == null || target == null) {
      return c2.search.netlas.scheme.Response.newBuilder()
          .setSuccess(false)
          .setDescription("Certificate is null")
          .build();
    }

    final Certificate baseCertificates = response.getCertificate();
    final var comp = new CertificateComparator();
    for (final Certificate targetCertificate : target) {
      if (comp.compare(targetCertificate, baseCertificates) == 0) {
        return c2.search.netlas.scheme.Response.newBuilder()
            .setSuccess(true)
            .setDescription("Certificate matched")
            .build();
      }
    }
    return c2.search.netlas.scheme.Response.newBuilder()
        .setSuccess(false)
        .setDescription("Certificate not matched")
        .build();
  }

  private c2.search.netlas.scheme.Response passPort(final StaticData data) {
    final List<Integer> targetPorts = data.getPort();
    if (data.getPort() == null || targetPorts == null) {
      return c2.search.netlas.scheme.Response.newBuilder()
          .setSuccess(false)
          .setDescription("Port is null")
          .build();
    }

    final Integer basePorts = response.getPort();
    final boolean pass = targetPorts.contains(basePorts);
    return c2.search.netlas.scheme.Response.newBuilder()
        .setSuccess(pass)
        .setDescription("Port matched")
        .build();
  }

  private c2.search.netlas.scheme.Response passHeader(final StaticData data) {
    final List<Headers> targetHeaders = data.getHeader();
    if (data.getHeader() == null || targetHeaders == null) {
      return c2.search.netlas.scheme.Response.newBuilder()
          .setSuccess(false)
          .setDescription("Header is null")
          .build();
    }

    final Headers baseHeaders = response.getHttp().getHeaders();
    final var comp = new HeadersComparator();
    for (final Headers targetHeader : targetHeaders) {
      if (comp.compare(targetHeader, baseHeaders) == 0) {
        return c2.search.netlas.scheme.Response.newBuilder()
            .setSuccess(true)
            .setDescription("Header matched")
            .build();
      }
    }
    return c2.search.netlas.scheme.Response.newBuilder()
        .setSuccess(false)
        .setDescription("Header not matched")
        .build();
  }

  private c2.search.netlas.scheme.Response passBody(final StaticData data) {
    final List<String> targetBody = data.getBodyAsSha256();
    final String baseBody = response.getHttp().getBodySha256();
    if (baseBody == null || targetBody == null) {
      return c2.search.netlas.scheme.Response.newBuilder()
          .setSuccess(false)
          .setDescription("Body is null")
          .build();
    }

    final boolean pass = targetBody.contains(baseBody);
    return c2.search.netlas.scheme.Response.newBuilder()
        .setSuccess(pass)
        .setDescription("Body matched")
        .build();
  }

  @Override
  public Results analyze(final StaticData data) {
    final Results results = new Results();
    final List<c2.search.netlas.scheme.Response> responses =
        List.of(passJarm(data), passCert(data), passPort(data), passHeader(data), passBody(data));
    results.setResponses(Map.of(getNameOfClass(data.getClass()), responses));
    return results;
  }

  private String getNameOfClass(final Class<?> clazz) {
    final Static annotation = clazz.getAnnotation(Static.class);
    String name = annotation.name();
    if (name == null || name.isEmpty()) {
      name = clazz.getSimpleName();
    }
    return name;
  }
}
