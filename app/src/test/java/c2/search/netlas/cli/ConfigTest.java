package c2.search.netlas.cli;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import org.junit.jupiter.api.Test;

public class ConfigTest {

  @Test
  public void testGetAndSave() {
    String fileName = "test.properties";
    Properties expected = new Properties();
    expected.setProperty("key1", "value1");
    expected.setProperty("key2", "value2");

    try (FileOutputStream output = new FileOutputStream(fileName)) {
      expected.store(output, null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    Config config = new Config(fileName);

    assertEquals("value1", config.get("key1"));
    assertEquals("value2", config.get("key2"));
    assertNull(config.get("key3"));

    config.save("key3", "value3");
    assertEquals("value3", config.get("key3"));

    File file = new File(fileName);
    if (file.exists()) {
      file.delete();
    }
  }
}
