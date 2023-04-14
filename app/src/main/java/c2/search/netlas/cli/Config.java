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
  private static final String NAME_ROOT_DIR = ".c2detect";
  private final Path parentDir = Paths.get(System.getProperty("user.home"), NAME_ROOT_DIR);
  private final Path filePath;
  private Properties properties;

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

  public void setProperties(final Properties properties) {
    this.properties = properties;
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
    createFileIfNotExists();
    try (InputStream input = Files.newInputStream(filePath)) {
      properties.load(input);
    } catch (final IOException e) {
      LOGGER.error("Error reading config file: {}", filePath, e);
    }
  }

  private void createFileIfNotExists() {
    try {
      createFile();
    } catch (final IOException e) {
      LOGGER.error("Error creating config file: {}", filePath, e);
    }
  }

  private void createFile() throws IOException {
    if (!Files.exists(parentDir)) {
      LOGGER.debug("Creating config directory: {}", parentDir);
      Files.createDirectories(parentDir);
    }

    LOGGER.debug("Creating config file: {}", filePath);
    Files.createFile(filePath);
  }

  public String getNameRootDir() {
    return NAME_ROOT_DIR;
  }

  protected final Properties createProperties() {
    return new Properties();
  }
}
