package c2.search.netlas.analyze;

import c2.search.netlas.comparator.CertificateComparator;
import c2.search.netlas.comparator.CustomListComparator;
import c2.search.netlas.comparator.HeadersComparator;
import c2.search.netlas.comparator.ListComparator;
import c2.search.netlas.scheme.Results;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;
import netlas.java.scheme.Response;

class StaticAnalyzerImpl implements StaticAnalyzer {
  private final Response response;

  public StaticAnalyzerImpl(final Response response) {
    this.response = response;
  }

  @Override
  public Results analyze(final List<StaticData> data) {
    return null;
  }

  private boolean passJarm(final StaticData data) {
    ListComparator listComparator = new ListComparator();
    List<String> baseJarms =
        response.getItems().stream()
            .map(item -> item.getData().getJarm())
            .filter(jarm -> jarm != null && !jarm.isEmpty())
            .collect(Collectors.toList());

    boolean passJarm = false;
    if (!baseJarms.isEmpty()) {
      List<String> targetJarms = data.getJarm();
      passJarm = listComparator.compare(baseJarms, targetJarms) == 0;
    }

    return passJarm;
  }

  private boolean passCert(final StaticData data) {
    List<Certificate> targetCertificates = data.getCertificate();
    List<Certificate> baseCertificates =
        response.getItems().stream()
            .map(item -> item.getData().getCertificate())
            .filter(certificate -> certificate != null)
            .collect(Collectors.toList());

    if (!baseCertificates.isEmpty()) {
      BiPredicate<Certificate, Certificate> certificateComparator =
          (c1, c2) -> new CertificateComparator().compare(c1, c2) == 0;
      CustomListComparator<Certificate, Certificate> customListComparator =
          new CustomListComparator<>(certificateComparator, targetCertificates);

      return customListComparator.compare(baseCertificates, targetCertificates) == 0;
    }
    return false;
  }

  private boolean passPort(final StaticData data) {
    List<Integer> targetPorts = data.getPort();
    List<Integer> basePorts =
        response.getItems().stream()
            .map(item -> item.getData().getPort())
            .filter(port -> port != null)
            .collect(Collectors.toList());

    if (!basePorts.isEmpty()) {
      BiPredicate<Integer, Integer> portComparator = (p1, p2) -> p1 == p2;
      CustomListComparator<Integer, Integer> customListComparator =
          new CustomListComparator<>(portComparator, targetPorts);
      return customListComparator.compare(basePorts, targetPorts) == 0;
    }
    return false;
  }

  private boolean passHeader(final StaticData data) {
    List<Headers> targetHeaders = data.getHeader();
    List<Headers> baseHeaders =
        response.getItems().stream()
            .map(item -> item.getData().getHttp().getHeaders())
            .filter(headers -> headers != null)
            .collect(Collectors.toList());

    if (!baseHeaders.isEmpty()) {
      BiPredicate<Headers, Headers> headerComparator =
          (h1, h2) -> new HeadersComparator().compare(h1, h2) == 0;
      CustomListComparator<Headers, Headers> customListComparator =
          new CustomListComparator<>(headerComparator, targetHeaders);
      return customListComparator.compare(baseHeaders, targetHeaders) == 0;
    }
    return false;
  }

  private boolean passBody(final StaticData data) {
    List<String> targetBody = data.getBodyAsSha256();
    List<String> baseBody =
        response.getItems().stream()
            .map(item -> item.getData().getHttp().getBody())
            .filter(body -> body != null)
            .collect(Collectors.toList());

    if (!baseBody.isEmpty()) {
      ListComparator listComparator = new ListComparator();
      return listComparator.compare(baseBody, targetBody) == 0;
    }
    return false;
  }

  @Override
  public Results analyze(final StaticData data) {
    Results results = new Results();

    var jarmResp = c2.search.netlas.scheme.Response.newBuilder().setSuccess(passJarm(data)).build();
    results.addResponse("passJarm", jarmResp);

    var certResp = c2.search.netlas.scheme.Response.newBuilder().setSuccess(passCert(data)).build();
    results.addResponse("passCert", certResp);

    var portResp = c2.search.netlas.scheme.Response.newBuilder().setSuccess(passPort(data)).build();
    results.addResponse("passPort", portResp);

    var headerResp =
        c2.search.netlas.scheme.Response.newBuilder().setSuccess(passHeader(data)).build();
    results.addResponse("passHeader", headerResp);

    var bodyResp = c2.search.netlas.scheme.Response.newBuilder().setSuccess(passBody(data)).build();
    results.addResponse("passBody", bodyResp);

    return results;
  }
}
