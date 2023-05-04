package c2.search.netlas.target.empire;

import java.security.NoSuchAlgorithmException;

public class Utils {
    public static boolean testEndpoint(
            final String endpoint, final int expectedStatus, final String expectedHash)
            throws NoSuchAlgorithmException {
        final String[] http = c2.search.netlas.target.bruteratel.Utils.getHttpResponse(String.format("http://%s", endpoint));
        final String[] https = c2.search.netlas.target.bruteratel.Utils.getHttpResponse(String.format("https://%s", endpoint));
        final String rbody = c2.search.netlas.target.bruteratel.Utils.getSHA256Hash(http[1]);
        final String rbody2 = c2.search.netlas.target.bruteratel.Utils.getSHA256Hash(https[1]);
        final int rcode = Integer.parseInt(http[0]);
        final int rcode2 = Integer.parseInt(https[0]);
        final boolean eqBody = rbody.equals(expectedHash) || rbody2.equals(expectedHash);
        final boolean eqStatus = rcode == expectedStatus && rcode2 == expectedStatus;
        return eqBody && eqStatus;
    }
}
