package c2.search.netlas.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
  private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
  private final String nameRootDir = ".c2detect";
  private final Path parentDir = Paths.get(System.getProperty("user.home"), nameRootDir);
  private final Path filePath;
  private final Properties properties;

  public Config(final String filePath) {
    this.filePath = parentDir.resolve(Paths.get(filePath));
    this.properties = createProperties();
    loadProperties();
  }

  public String get(final String key) {
    return properties.getProperty(key);
  }

  public Path getFilePath() {
    return filePath;
  }

  public void save(final String key, final String value) {
    properties.setProperty(key, value);
    try (OutputStream output = Files.newOutputStream(filePath)) {
      properties.store(output, null);
    } catch (final IOException e) {
      LOGGER.error("Error writing config file: {}", filePath, e);
    }
  }

  private void loadProperties() {
    if (!Files.exists(filePath)) {
      LOGGER.warn("Config file not found: {}", filePath);
      createFile();
    }
    try (InputStream input = Files.newInputStream(filePath)) {
      properties.load(input);
    } catch (final IOException e) {
      LOGGER.error("Error reading config file: {}", filePath, e);
    }
  }

  private void createFile() {
    LOGGER.error("Config file not found: {}", filePath);
    if (!Files.exists(parentDir)) {
      LOGGER.debug("Creating config directory: {}", parentDir);
      try {
        Files.createDirectories(parentDir);
      } catch (final IOException e) {
        LOGGER.error("Error creating config directory: {}", parentDir, e);
        return;
      }
    }

    LOGGER.debug("Creating config file: {}", filePath);
    try {
      Files.createFile(filePath);
    } catch (final IOException e) {
      LOGGER.error("Error creating config file: {}", filePath, e);
    }
  }

  protected Properties createProperties() {
    return new Properties();
  }

  public String getNameRootDir() {
    return nameRootDir;
  }
}
