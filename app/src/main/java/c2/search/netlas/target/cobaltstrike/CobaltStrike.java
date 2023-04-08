package c2.search.netlas.target.cobaltstrike;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Arrays;
import java.util.List;

@Detect(name = "CobaltStrike")
public class CobaltStrike {
  @Wire private Host host;
  @Wire private NetlasWrapper netlasWrapper;

  @Test
  public boolean testJarm() {
    final String jarm = "2ad2ad16d2ad2ad00042d42d00042ddb04deffa1705e2edc44cae1ed24a4da";
    String tjarm;
    try {
      tjarm = netlasWrapper.getJarm();
    } catch (JsonProcessingException e) {
      tjarm = null;
    }

    return jarm.equals(tjarm);
  }

  @Test
  public boolean defaultCertFieldTeamServer() throws JsonMappingException, JsonProcessingException {
    // * subject: C=US; ST=Washington; L=Redmond; O=Microsoft Corporation;
    // OU=Microsoft Corporation; CN=Outlook.live.com
    // * issuer: C=US; ST=Washington; L=Redmond; O=Microsoft Corporation;
    // OU=Microsoft Corporation; CN=Outlook.live.com
    final String country = "US";
    final String state = "Washington";
    final String city = "Redmond";
    final String organization = "Microsoft Corporation";
    final String organizationUnit = "Microsoft Corporation";
    final String commonName = "Outlook.live.com";

    final List<String> subjectCountry = netlasWrapper.getCertSubjectCountry();
    final List<String> subjectState = netlasWrapper.getCertSubjectProvince();
    final List<String> subjectCity = netlasWrapper.getCertSubjectLocality();
    final List<String> subjectOrganization = netlasWrapper.getCertSubjectOrganization();
    final List<String> subjectOrganizationUnit = netlasWrapper.getCertSubjectOrganizationUnit();
    final List<String> subjectCommonName = netlasWrapper.getCertSubjectCommonName();

    final List<String> issuerCountry = netlasWrapper.getCertIssuerCountry();
    final List<String> issuerState = netlasWrapper.getCertIssuerProvince();
    final List<String> issuerCity = netlasWrapper.getCertIssuerLocality();
    final List<String> issuerOrganization = netlasWrapper.getCertIssuerOrganization();
    final List<String> issuerOrganizationUnit = netlasWrapper.getCertIssuerOrganizationUnit();
    final List<String> issuerCommonName = netlasWrapper.getCertIssuerCommonName();

    final List<List<String>> subject =
        Arrays.asList(
            subjectCountry,
            subjectState,
            subjectCity,
            subjectOrganization,
            subjectOrganizationUnit,
            subjectCommonName);
    final List<List<String>> issuer =
        Arrays.asList(
            issuerCountry,
            issuerState,
            issuerCity,
            issuerOrganization,
            issuerOrganizationUnit,
            issuerCommonName);

    return allEqual(subject, country, state, city, organization, organizationUnit, commonName)
        && allEqual(issuer, country, state, city, organization, organizationUnit, commonName);
  }

  @Test
  public boolean defaultCertFieldListener() throws JsonMappingException, JsonProcessingException {
    // * subject: C=; ST=; L=; O=; OU=; CN=
    // * issuer: C=; ST=; L=; O=; OU=; CN=
    final String country = "";
    final String state = "";
    final String city = "";
    final String organization = "";
    final String organizationUnit = "";
    final String commonName = "";

    final List<String> subjectCountry = netlasWrapper.getCertSubjectCountry();
    final List<String> subjectState = netlasWrapper.getCertSubjectProvince();
    final List<String> subjectCity = netlasWrapper.getCertSubjectLocality();
    final List<String> subjectOrganization = netlasWrapper.getCertSubjectOrganization();
    final List<String> subjectOrganizationUnit = netlasWrapper.getCertSubjectOrganizationUnit();
    final List<String> subjectCommonName = netlasWrapper.getCertSubjectCommonName();

    final List<String> issuerCountry = netlasWrapper.getCertIssuerCountry();
    final List<String> issuerState = netlasWrapper.getCertIssuerProvince();
    final List<String> issuerCity = netlasWrapper.getCertIssuerLocality();
    final List<String> issuerOrganization = netlasWrapper.getCertIssuerOrganization();
    final List<String> issuerOrganizationUnit = netlasWrapper.getCertIssuerOrganizationUnit();
    final List<String> issuerCommonName = netlasWrapper.getCertIssuerCommonName();

    final List<List<String>> subject =
        Arrays.asList(
            subjectCountry,
            subjectState,
            subjectCity,
            subjectOrganization,
            subjectOrganizationUnit,
            subjectCommonName);
    final List<List<String>> issuer =
        Arrays.asList(
            issuerCountry,
            issuerState,
            issuerCity,
            issuerOrganization,
            issuerOrganizationUnit,
            issuerCommonName);

    return allEqual(subject, country, state, city, organization, organizationUnit, commonName)
        && allEqual(issuer, country, state, city, organization, organizationUnit, commonName);
  }

  private boolean allEqual(List<List<String>> lists, String... expectedValues) {
    boolean result;
    if (lists.size() != expectedValues.length) {
      result = false;
    } else {
      for (int i = 0; i < lists.size(); i++) {
        if (!lists.get(i).contains(expectedValues[i])) {
          result = false;
          break;
        }
      }
      result = true;
    }
    return result;
  }
}
