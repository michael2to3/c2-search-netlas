package c2.search.netlas.cli;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
  private Properties props;
  private String fileName;

  public Config(String fileName) {
    this.fileName = fileName;
    props = new Properties();

    try (FileInputStream input = new FileInputStream(fileName)) {
      props.load(input);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String get(String key) {
    String value = props.getProperty(key);
    if (value == null) {
      try (FileInputStream input = new FileInputStream(fileName)) {
        props.load(input);
        value = props.getProperty(key);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (value == null) {
      value = System.getenv(key);
    }
    if (value == null) {
      String upperKey = key.toUpperCase().replace('.', '_');
      value = System.getenv(upperKey);
    }
    return value;
  }

  public void save(String key, String value) {
    props.setProperty(key, value);
    try (FileOutputStream output = new FileOutputStream(fileName)) {
      props.store(output, null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
