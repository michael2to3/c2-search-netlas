package c2.search.netlas.target.posh;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.target.NetlasWrapper;
import c2.search.netlas.scheme.Host;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.List;

@Detect (name = "Posh")
public class Posh {
    @Wire
    private Host host;
    @Wire
    private NetlasWrapper netlasWrapper;
    public Posh() {}

    @Test
    public boolean checkJarm() throws JsonMappingException, JsonProcessingException {
        final String jarm = "2ad2ad0002ad2ad22c42d42d000000faabb8fd156aa8b4d8a37853e1063261";
        final String rjarm = netlasWrapper.getJarm();
        return rjarm.equals(jarm);
    }

    @Test
    public boolean checkCertSubj() throws JsonMappingException, JsonProcessingException {
        final String[] subjectFields = {
                "US",
                "Minnesota",
                "Minnetonka",
                "Pajfds",
                "Jethpro",
                "P18055077",
        };
        final String[] issuerFields = {
                "US",
                "Minnesota",
                "Minnetonka",
                "Pajfds",
                "Jethpro",
                "P18055077",
        };
        return Utils.verifyCertFields(netlasWrapper, subjectFields, issuerFields);
    }

    @Test
    public boolean checkListenerServHead() throws JsonMappingException, JsonProcessingException {
        final List<String> servers = netlasWrapper.getServers();
        final List<String> types = netlasWrapper.getContentType();
        final String baseType = "text/html";
        final String base = "Apache";
        boolean result = false;
        for (final String server : servers) {
            if (server.contains(base)) {
                result = true;
            }
        }
        return result && types.equals(baseType);
    }

    @Test
    public boolean checkBodyResp() throws JsonMappingException, JsonProcessingException {
        final String bodyResponse = "404";
        final int statusCode = 404;
        final int nstatusCode = netlasWrapper.getStatusCode();
        final boolean isDefaultResponse = netlasWrapper.getBody().equals(bodyResponse);
        final boolean isCorrectStatus = statusCode == nstatusCode;
        return isDefaultResponse && isCorrectStatus;
    }
}
