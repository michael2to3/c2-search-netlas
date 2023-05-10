package c2.search.netlas.c2detect;

import c2.search.netlas.NetlasCache;
import c2.search.netlas.execute.Execute;
import c2.search.netlas.execute.FieldValues;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Results;
import netlas.java.Netlas;

public class C2DetectImpl implements C2Detect {
  private final Host targetHost;

  public C2DetectImpl(final Host targetHost) {
    this.targetHost = targetHost;
  }

  @Override
  public Results run() {
    final FieldValues fieldValues = createFieldValues();
    final Execute executor = new Execute(fieldValues, this.targetHost);
    return executor.run();
  }

  private FieldValues createFieldValues() {
    final FieldValues fields = new FieldValues();
    fields.set(Host.class, this.targetHost);
    fields.set(Netlas.class, NetlasCache.getNetlas());
    fields.set(NetlasCache.class, NetlasCache.getInstance());
    return fields;
  }
}
