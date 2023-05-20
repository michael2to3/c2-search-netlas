package c2.search.netlas.target.empire;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Detect(name = "Empire")
public class Empire {
    @Wire
    private Host host;

    public Empire() {
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
