package c2.search.netlas.target.villain;

import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Arrays;
import java.util.List;

final class CertificateUtils {
  private CertificateUtils() {}

  public static boolean verifyCertFieldsSubject(
      final NetlasWrapper netlasWrapper, final String[] subjectFields)
      throws JsonMappingException, JsonProcessingException {
    final List<List<String>> subject =
        Arrays.asList(
            netlasWrapper.getCertSubjectCountry(),
            netlasWrapper.getCertSubjectProvince(),
            netlasWrapper.getCertSubjectLocality(),
            netlasWrapper.getCertSubjectOrganization(),
            netlasWrapper.getCertSubjectOrganizationUnit(),
            netlasWrapper.getCertSubjectCommonName());
    return allFieldsEqual(subject, subjectFields);
  }

  public static boolean verifyCertFieldsIssuer(
      final NetlasWrapper netlasWrapper, final String[] issuerFields)
      throws JsonMappingException, JsonProcessingException {
    final List<List<String>> issuer =
        Arrays.asList(
            netlasWrapper.getCertIssuerCountry(),
            netlasWrapper.getCertIssuerProvince(),
            netlasWrapper.getCertIssuerLocality(),
            netlasWrapper.getCertIssuerOrganization(),
            netlasWrapper.getCertIssuerOrganizationUnit(),
            netlasWrapper.getCertIssuerCommonName());
    return allFieldsEqual(issuer, issuerFields);
  }

  private static boolean allFieldsEqual(final List<List<String>> fields, final String[] values) {
    boolean result = true;
    for (int i = 0; i < values.length; i++) {
      final String value = values[i];
      if (!value.isEmpty() && !fields.get(i).contains(value)) {
        result = false;
        break;
      }
    }
    return result;
  }
}
