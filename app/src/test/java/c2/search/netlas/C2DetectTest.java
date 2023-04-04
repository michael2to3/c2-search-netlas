package c2.search.netlas;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

import c2.search.netlas.cli.Config;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.Results;
import c2.search.netlas.scheme.Version;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class C2DetectTest {
  private static final String API = new Config("config.properties").get("api.key");

  @BeforeAll
  public static void setup() {
    mockStatic(Config.class);
  }
}
