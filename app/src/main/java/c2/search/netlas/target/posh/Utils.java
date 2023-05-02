package c2.search.netlas.target.posh;

import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.util.Arrays;
import java.util.List;

public class Utils {
    private Utils() {}
    public static boolean compareList(final List<List<String>> lhs, final String[] rhs) {
        boolean result = true;
        for (int i = 0; i < lhs.size(); ++i) {
            if (!lhs.get(i).contains(rhs[i])) {
                result = false;
                break;
            }
        }
        return result;
    }
    public static boolean verifyCertFields(
            final NetlasWrapper netlasWrapper, final String[] subjectFields, final String[] issuerFields)
            throws JsonMappingException, JsonProcessingException {
        final List<String> subCountry = netlasWrapper.getCertSubjectCountry();
        final List<String> subState = netlasWrapper.getCertSubjectProvince();
        final List<String> subCity = netlasWrapper.getCertSubjectLocality();
        final List<String> subOrg = netlasWrapper.getCertSubjectOrganization();
        final List<String> subOrgUnit = netlasWrapper.getCertSubjectOrganizationUnit();
        final List<String> subCommonName = netlasWrapper.getCertSubjectCommonName();

        final List<String> issCountry = netlasWrapper.getCertIssuerCountry();
        final List<String> issState = netlasWrapper.getCertIssuerProvince();
        final List<String> issCity = netlasWrapper.getCertIssuerLocality();
        final List<String> issOrg = netlasWrapper.getCertIssuerOrganization();
        final List<String> issOrgUnit = netlasWrapper.getCertIssuerOrganizationUnit();
        final List<String> issCommonName = netlasWrapper.getCertIssuerCommonName();

        final List<List<String>> subject =
                Arrays.asList(subCountry, subState, subCity, subOrg, subOrgUnit, subCommonName);
        final List<List<String>> issuer =
                Arrays.asList(issCountry, issState, issCity, issOrg, issOrgUnit, issCommonName);

        return compareList(subject, subjectFields) && compareList(issuer, issuerFields);
    }
}
