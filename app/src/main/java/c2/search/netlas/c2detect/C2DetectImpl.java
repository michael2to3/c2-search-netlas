package c2.search.netlas.c2detect;

import c2.search.netlas.execute.Execute;
import c2.search.netlas.execute.FieldValues;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Results;
import c2.search.netlas.target.NetlasWrapper;
import netlas.java.Netlas;
import netlas.java.exception.NetlasRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class C2DetectImpl implements C2Detect {
  private static final Logger LOGGER = LoggerFactory.getLogger(C2DetectImpl.class);
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
    final Results results = executor.run();
    return results;
  }

  private FieldValues createFieldValues() {
    final FieldValues fields = new FieldValues();
    fields.setField(Host.class, this.targetHost);
    fields.setField(Netlas.class, this.netlas);
    fields.setField(NetlasWrapper.class, createNetlasWrapper());
    return fields;
  }

  private NetlasWrapper createNetlasWrapper() {
    NetlasWrapper netlas = null;
    try {
      netlas = new NetlasWrapper(this.netlas, this.targetHost);
    } catch (final NetlasRequestException e) {
      LOGGER.error("Failed to create NetlasWrapper", e);
    }
    return netlas;
  }
}
