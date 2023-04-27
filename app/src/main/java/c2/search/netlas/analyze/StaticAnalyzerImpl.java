package c2.search.netlas.analyze;

import c2.search.netlas.comparator.CertificateComparator;
import c2.search.netlas.comparator.HeadersComparator;
import c2.search.netlas.comparator.ListComparator;
import c2.search.netlas.scheme.Results;
import java.util.List;
import netlas.java.scheme.*;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;

public class StaticAnalyzerImpl implements StaticAnalyzer {
  private final Data response;

  public StaticAnalyzerImpl(final Data response) {
    this.response = response;
  }

  @Override
  public Results analyze(final List<StaticData> data) {
    return null;
  }

  private boolean passJarm(final StaticData data) {
    final String baseJarms = response.getJarm();
    return data.getJarm().contains(baseJarms);
  }

  private boolean passCert(final StaticData data) {
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
    List<Integer> targetPorts = data.getPort();
    Integer basePorts = response.getPort();
    return targetPorts.contains(basePorts);
  }

  private boolean passHeader(final StaticData data) {
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
    List<String> targetBody = data.getBodyAsSha256();
    List<String> baseBody = data.getBodyAsSha256();

    ListComparator listComparator = new ListComparator();
    return listComparator.compare(baseBody, targetBody) == 0;
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
