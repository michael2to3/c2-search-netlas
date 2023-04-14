package c2.search.netlas.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
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

  @Test
  void testGetFilePath() {
    Path expectedPath = parentDir.resolve(config.getNameRootDir()).resolve("test.properties");
    assertEquals(expectedPath, config.getFilePath());
  }

  @Test
  void testSave() throws IOException {
    String key = "key";
    String value = "value";

    config.save(key, value);

    verify(properties).setProperty(key, value);
    verify(properties).store(any(OutputStream.class), isNull());
  }
}
