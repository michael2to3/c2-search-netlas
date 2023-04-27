package c2.search.netlas.c2detect;

import c2.search.netlas.execute.Execute;
import c2.search.netlas.execute.FieldValues;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Results;
import netlas.java.Netlas;
import netlas.java.exception.NetlasRequestException;
import netlas.java.scheme.Data;
import netlas.java.scheme.Response;
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
    final Data data = getData();
    final FieldValues fieldValues = createFieldValues(data);
    final Execute executor = new Execute(fieldValues, data);
    return executor.run();
  }

  private FieldValues createFieldValues(final Data data) {
    final FieldValues fields = new FieldValues();
    fields.set(Host.class, this.targetHost);
    fields.set(Netlas.class, this.netlas);
    fields.set(Data.class, data);
    return fields;
  }

  private Data getData() {
    try {
      Response resp =
          netlas.response(
              String.format(
                  "host:\"%s\" AND port:%s AND path:\"/\"",
                  targetHost.getTarget(), targetHost.getPort()),
              0,
              null,
              null,
              false);
      return resp.getItems().get(0).getData();
    } catch (NetlasRequestException e) {
      LOGGER.error("Error while sending request", e);
    }
    return null;
  }
}
