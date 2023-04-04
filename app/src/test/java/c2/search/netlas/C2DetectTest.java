package c2.search.netlas;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import c2.search.netlas.classscanner.FieldValues;
import c2.search.netlas.cli.CLArgumentsManager;
import c2.search.netlas.cli.Config;
import c2.search.netlas.scheme.Host;
import java.net.Socket;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class C2DetectTest {
  private static final String API = new Config("config.properties").get("api.key");

  public CLArgumentsManager getParseCmdArgs(String[] args) throws ParseException {
    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = parser.parse(setupOptions(), args);

    CLArgumentsManager parseCmdArgs = new CLArgumentsManager(cmd, new Config("test.prop"));
    return parseCmdArgs;
  }

  private Options setupOptions() {
    Options options = new Options();
    Option setOption =
        Option.builder("s")
            .longOpt("set")
            .hasArg(true)
            .argName("API_KEY")
            .desc("Set the API key to use for the application")
            .build();
    Option targetOption =
        Option.builder("t")
            .longOpt("target")
            .hasArg(true)
            .argName("TARGET_DOMAIN")
            .desc("Set the target domain for the application")
            .build();
    Option portOption =
        Option.builder("p")
            .longOpt("port")
            .hasArg(true)
            .argName("TARGET_PORT")
            .desc("Set the target port for the application")
            .build();
    Option printVerbosOption =
        Option.builder("v").longOpt("verbose").hasArg(false).desc("Print verbose output").build();
    Option helpOption = Option.builder("h").longOpt("help").desc("Print this help message").build();

    List<Option> list = List.of(setOption, targetOption, portOption, printVerbosOption, helpOption);
    for (Option option : list) {
      options.addOption(option);
    }
    return options;
  }

  @Test
  public void testSettersAndGetters() throws ParseException {
    CLArgumentsManager manager = getParseCmdArgs(new String[] {});
    C2Detect c2Detect = new C2Detect(manager, System.out);
    c2Detect.setFields(new FieldValues());
  }

  @Mock Host host;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetSocket() throws Exception {
    Socket expectedSocket = mock(Socket.class);
    when(host.getTarget()).thenReturn("localhost");
    when(host.getPort()).thenReturn(8080);
    doNothing().when(expectedSocket).setSoTimeout(1000);

    Socket actualSocket = C2Detect.getSocket(host, 1000);

    assertNull(actualSocket);
  }

  @Test
  void testGetSocketIOException() throws Exception {
    when(host.getTarget()).thenReturn("localhost");
    when(host.getPort()).thenReturn(8080);

    Socket actualSocket = C2Detect.getSocket(host, 1000);

    assertNull(actualSocket);
  }
}
