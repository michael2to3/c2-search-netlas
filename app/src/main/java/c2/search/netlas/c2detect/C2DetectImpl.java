package c2.search.netlas.c2detect;

import c2.search.netlas.execute.Execute;
import c2.search.netlas.execute.FieldValues;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Results;
import netlas.java.Netlas;

public class C2DetectImpl implements C2Detect {
  private final Host targetHost;
  private final Netlas netlas;

  public C2DetectImpl(final Host targetHost, final Netlas netlas) {
    this.targetHost = targetHost;
    this.netlas = netlas;
  }

  @Override
  public Results run() {
    final FieldValues fieldValues = createFieldValues();

    final Execute executor = new Execute(fieldValues);
    return executor.run();
  }

  private FieldValues createFieldValues() {
    final FieldValues fields = new FieldValues();
    fields.setField(Host.class, this.targetHost);
    fields.setField(Netlas.class, this.netlas);
    return fields;
  }
}
