package c2.search.netlas.cli;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
  private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
  private final String fileName;
  private final Properties props;

  public Config(final String fileName) {
    this.fileName = fileName;
    this.props = new Properties();

    try (InputStream input = Files.newInputStream(Paths.get(fileName))) {
      props.load(input);
    } catch (final FileNotFoundException e) {
      LOGGER.error("Config file not found: {}", fileName, e);
    } catch (final IOException e) {
      LOGGER.error("Error reading config file: {}", fileName, e);
    }
  }

  public String get(final String key) {
    String value = props.getProperty(key);
    if (value == null) {
      value = System.getenv(key);
    }
    if (value == null) {
      final String upperKey = key.toUpperCase(Locale.getDefault()).replace('.', '_');
      value = System.getenv(upperKey);
    }
    return value;
  }

  public void save(final String key, final String value) {
    props.setProperty(key, value);
    try (OutputStream output = Files.newOutputStream(Paths.get(fileName))) {
      props.store(output, null);
    } catch (final IOException e) {
      LOGGER.error("Error writing config file: {}", fileName, e);
    }
  }
}
