package c2.search.netlas.target.empire;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.Version;
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
    public Response checkJarm() throws JsonMappingException, JsonProcessingException {
        final List<String> jarmv5 =
                List.of("0ad0ad0000ad0ad00042d42d0000007320ccd9701dbccd7024a4f866f0cfd9");

        final String responseJarm = netlasWrapper.getJarm();

        String minVersion = null;
        boolean detect = false;
        if (checkJarm(responseJarm, jarmv5)) {
            minVersion = "5.x.x";
            detect = true;
        }
        return Response.newBuilder().setSuccess(detect).setVersion(new Version("", minVersion)).build();
    }

    @Test
    public boolean checkHeaders() throws JsonMappingException, JsonProcessingException {
        final List<String> servers = netlasWrapper.getServers();
        final List<String> defaultHeaders =
                List.of("Werkzeug", "Python", "Microsoft-IIS");
        int defaultHeadersNum = defaultHeaders.size();
        for (final String server : servers) {
            for (final String header : defaultHeaders) {
                if (!server.toLowerCase(Locale.getDefault()).contains(header)) {
                    defaultHeadersNum--;
                }
            }
        }

        final int status = netlasWrapper.getStatusCode();

        return defaultHeadersNum == 0 & STATUS_SUCCESFULL == status;
    }

    @Test(extern = true)
    public boolean checkDocumentationPage() throws IOException, NoSuchAlgorithmException {
        final int result = 200;
        final String sha256documentation =
                "e87aa3bc0789083c0b05e040bdc309de0e79fc2eb12b8c04e853b1e8a4eac4f4";
        return Utils.testEndpoint(host.toString() + "/openapi.json", result, sha256documentation);
    }
}