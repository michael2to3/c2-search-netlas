package c2.search.netlas;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

import c2.search.netlas.cli.Config;
import org.junit.jupiter.api.BeforeAll;

public class C2DetectTest {
  private static final String API = new Config("config.properties").get("api.key");

  @BeforeAll
  public static void setup() {
    mockStatic(Config.class);
  }
}
