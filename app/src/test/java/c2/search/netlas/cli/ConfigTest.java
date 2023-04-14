package c2.search.netlas.cli;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ConfigTest {
  private Path parentDir;
  private Config config;
  private Properties properties;

  @BeforeEach
  void setUp(@TempDir Path tempDir) {
    parentDir = tempDir;
    System.setProperty("user.home", parentDir.toString());
    properties = spy(Properties.class);

    config =
        new Config("test.properties") {
          @Override
          protected Properties createProperties() {
            return properties;
          }
        };
  }

  @Test
  void testGet() {
    String key = "key";
    String value = "value";
    when(properties.getProperty(key)).thenReturn(value);

    String result = config.get(key);
    assertEquals(value, result);
  }
}
