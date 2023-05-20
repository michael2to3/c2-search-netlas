package c2.search.netlas.target.empire;

import c2.search.netlas.analyze.StaticData;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;
import java.util.List;

public class EmpireStatic implements StaticData {
    public EmpireStatic() {
    }

    @Override
    public List<String> getJarm() {
        return List.of("0ad0ad0000ad0ad00042d42d0000007320ccd9701dbccd7024a4f866f0cfd9");
    }

    @Override
    public List<Certificate> getCertificate() {
        return List.of();
    }

    @Override
    public List<Integer> getPort() {
        return List.of();
    }

    @Override
    public List<Headers> getHeader() {
        final Headers header = new Headers();
        header.setHeader("Content-Type", List.of("text/html; charset-utf-8"));
        header.setHeader("Content-Length", List.of("836"));
        header.setHeader("server", List.of("Werkzeug", "Python", "Microsoft-IIS"));
        return List.of(header);
    }

    @Override
    public List<String> getBodyAsSha256() {
        return List.of(
                "e2b93e26a115d641baaca4f652db4bc64d5cac5c7479a6d4d1d08673fb410305",
                "48c3843f57eceab2613c42ed65bff6d8701b3cfe12534220702cf2a895e45a79",
                "e87aa3bc0789083c0b05e040bdc309de0e79fc2eb12b8c04e853b1e8a4eac4f4");
    }
}
