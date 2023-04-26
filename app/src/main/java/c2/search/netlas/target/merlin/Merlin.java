package c2.search.netlas.target.merlin;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.target.NetlasWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.List;

@Detect(name = "Merlin")
public class Merlin {
    @Wire
    private Host host;
    @Wire
    private NetlasWrapper netlasWrapper;

    public Merlin() {}

    public void setHost(final Host host) {
        this.host = host;
    }

    public void setNetlasWrapper(final NetlasWrapper netlasWrapper) {
        this.netlasWrapper = netlasWrapper;
    }

    @Test
    public boolean checkJarm() throws JsonMappingException, JsonProcessingException {
        final String jarm = "3fd21b20d00000021c43d21b21b43de0a012c76cf078b8d06f4620c2286f5e";
        final String rjarm = netlasWrapper.getJarm();
        return rjarm.equals(jarm);
    }
    @Test
    public boolean checkCertSubj() throws JsonMappingException, JsonProcessingException {
        final String subject = "";
        final List<String> subj = netlasWrapper.getCertSubjectCountry();
        return subj.contains(subject);
    }

    @Test
    public boolean checkHeaders() throws JsonMappingException, JsonProcessingException {
        final int contentLen = 0;
        final String contentType = "text/html";
        final boolean isEmptyServer = netlasWrapper.getServers().isEmpty();
        final boolean isDefaultLen = netlasWrapper.getContentLength().contains(contentLen);
        final boolean isDefaultType = netlasWrapper.getContentType().contains(contentType);
        return isEmptyServer && isDefaultLen && isDefaultType;
    }

    @Test
    public boolean checkBodyResp() throws JsonMappingException, JsonProcessingException {
        final String bodyResponse = "404";
        final int statusCode = 404;
        final int nstatusCode = netlasWrapper.getStatusCode();
        final boolean isDefaultResponse = netlasWrapper.getBody().contains(bodyResponse);
        final boolean isCorrectStatus = (statusCode == nstatusCode);
        return isDefaultResponse && isCorrectStatus;
    }
}
