package c2.search.netlas.analyze;

import c2.search.netlas.annotation.Static;
import c2.search.netlas.comparator.CertificateComparator;
import c2.search.netlas.comparator.HeadersComparator;
import c2.search.netlas.comparator.ListComparator;
import c2.search.netlas.scheme.Results;
import java.util.ArrayList;
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

  private boolean passJarm(final StaticData data) {
    if (data.getJarm() == null) {
      return false;
    }
    final String baseJarms = response.getJarm();
    return data.getJarm().contains(baseJarms);
  }

  private boolean passCert(final StaticData data) {
    if (data.getCertificate() == null) {
      return false;
    }

    List<Certificate> targetCertificates = data.getCertificate();
    Certificate baseCertificates = response.getCertificate();
    var comp = new CertificateComparator();
    for (Certificate targetCertificate : targetCertificates) {
      if (comp.compare(targetCertificate, baseCertificates) == 0) {
        return true;
      }
    }
    return false;
  }

  private boolean passPort(final StaticData data) {
    if (data.getPort() == null) {
      return false;
    }

    List<Integer> targetPorts = data.getPort();
    Integer basePorts = response.getPort();
    return targetPorts.contains(basePorts);
  }

  private boolean passHeader(final StaticData data) {
    if (data.getHeader() == null) {
      return false;
    }

    List<Headers> targetHeaders = data.getHeader();
    Headers baseHeaders = response.getHttp().getHeaders();
    var comp = new HeadersComparator();
    for (Headers targetHeader : targetHeaders) {
      if (comp.compare(targetHeader, baseHeaders) == 0) {
        return true;
      }
    }
    return false;
  }

  private boolean passBody(final StaticData data) {
    if (data.getBodyAsSha256() == null) {
      return false;
    }

    List<String> targetBody = data.getBodyAsSha256();
    List<String> baseBody = data.getBodyAsSha256();

    ListComparator listComparator = new ListComparator();
    return listComparator.compare(baseBody, targetBody) == 0;
  }

  @Override
  public Results analyze(final StaticData data) {
    Results results = new Results();
    List<c2.search.netlas.scheme.Response> responses = new ArrayList<>();
    List<Boolean> passed =
        List.of(passJarm(data), passCert(data), passPort(data), passHeader(data), passBody(data));
    for (Boolean passedItem : passed) {
      responses.add(c2.search.netlas.scheme.Response.newBuilder().setSuccess(passedItem).build());
    }
    results.setResponses(Map.of(getNameOfClass(data.getClass()), responses));
    return results;
  }

  private String getNameOfClass(final Class<?> clazz) {
    Static annotation = clazz.getAnnotation(Static.class);
    String name = annotation.name();
    if (name == null || name.isEmpty()) {
      name = clazz.getSimpleName();
    }
    return name;
  }
}
