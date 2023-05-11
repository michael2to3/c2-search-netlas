package c2.search.netlas.target.merlin;

import c2.search.netlas.analyze.StaticData;
import c2.search.netlas.annotation.Static;
import java.util.List;
import netlas.java.scheme.Certificate;
import netlas.java.scheme.Headers;
import netlas.java.scheme.Subject;

@Static(name = "Merlin")
public class MerlinStatic implements StaticData {
    public MerlinStatic() {}

    @Override
    public List<String> getJarm() {
        final String jarm = "3fd21b20d00000021c43d21b21b43de0a012c76cf078b8d06f4620c2286f5e";
        return List.of(jarm);
    }
    @Override
    public List<Certificate> getCertificate() {
        final Subject subject = new Subject();
        final Certificate cert = new Certificate();
        cert.setSubject(subject);
        return List.of(cert);
    }
    @Override
    public List<Integer> getPort() { return List.of(); }
    @Override
    public List<Headers> getHeader() {
        final Headers header = new Headers();
        header.setHeader("Content-Type", List.of("text/html"));
        header.setHeader("Content-Length", List.of("0"));
        return List.of(header);
    }
    @Override
    public List<String> getBodyAsSha256() { return List.of(); }
}
