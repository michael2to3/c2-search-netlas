package c2.search.netlas.target.empire;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import c2.search.netlas.target.bruteratel.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

@Detect(name = "Empire")
public class Empire {
    private static final int STATUS_SUCCESFULL = 200;
    @Wire
    private Host host;
    @Wire
    private NetlasWrapper netlasWrapper;

    public Empire() {
    }

    @Test
    public boolean checkDefaultBodyResponse() throws JsonMappingException, JsonProcessingException {
        final String body = "404 page not found";
        final String rbody = netlasWrapper.getBody();
        final int statusCode = 404;
        final int rstatusCode = netlasWrapper.getStatusCode();

        final List<String> servers = netlasWrapper.getServers();
        final boolean hasServerHeader = servers != null && !servers.isEmpty();

        return rbody.contains(body) && rstatusCode == statusCode && !hasServerHeader;
    }

    private boolean checkJarm(final String body, final List<String> jarms) {
        boolean isJarm = false;
        for (final String jarm : jarms) {
            if (body.contains(jarm)) {
                isJarm = true;
                break;
            }
        }
        return isJarm;
    }

    @Test
    public boolean checkJarm() throws JsonMappingException, JsonProcessingException {
        final List<String> jarm =
                List.of("0ad0ad0000ad0ad00042d42d0000007320ccd9701dbccd7024a4f866f0cfd9");
        final String responseJarm = netlasWrapper.getJarm();
        return checkJarm(responseJarm, jarm);
    }

    @Test
    public boolean checkHeaders() throws JsonMappingException, JsonProcessingException {
        final List<String> servers = netlasWrapper.getServers();
        final List<String> defaultHeaders =
                List.of("Werkzeug", "Python", "Microsoft-IIS");
        int defaultHeadersNum = 0;
        for (final String server : servers) {
            for (final String header : defaultHeaders) {
                if (!server.toLowerCase(Locale.getDefault()).contains(header)) {
                    defaultHeadersNum++;
                }
            }
        }

        final int status = netlasWrapper.getStatusCode();

        return defaultHeadersNum > defaultHeaders.size() & STATUS_SUCCESFULL == status;
    }

    @Test(extern = true)
    public boolean checkDocumentationPage() throws IOException, NoSuchAlgorithmException {
        final int result = 200;
        final String sha256doc =
                "e87aa3bc0789083c0b05e040bdc309de0e79fc2eb12b8c04e853b1e8a4eac4f4";
        return Utils.testEndpoint(host.toString() + "/openapi.json", result, sha256doc);
    }

    @Test(extern = true)
    public boolean badNotFound() throws IOException, NoSuchAlgorithmException {
        final int result = 404;
        final String sha256notfound =
                "48c3843f57eceab2613c42ed65bff6d8701b3cfe12534220702cf2a895e45a79";
        return Utils.testEndpoint(host.toString(), result, sha256notfound);
    }
}
