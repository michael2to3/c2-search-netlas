package c2.search.netlas.cli;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
  private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
  private Properties props;
  private String fileName;

  public Config(String fileName) {
    this.fileName = fileName;
    props = new Properties();

    try (FileInputStream input = new FileInputStream(fileName)) {
      props.load(input);
    } catch (FileNotFoundException e) {
      LOGGER.error("Config file not found: {}", fileName);
    } catch (IOException e) {
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
    } catch (FileNotFoundException e) {
      LOGGER.error("Config file not found: {}", fileName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
