package c2.search.netlas.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultConfigManager implements ConfigManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConfigManager.class);
  private static final String NAME_ROOT_DIR = ".c2detect";
  private final Path parentDir = Paths.get(System.getProperty("user.home"), NAME_ROOT_DIR);
  private final Path filePath;
  private Properties properties;

  public DefaultConfigManager(final String filePath) {
    this.filePath = parentDir.resolve(Paths.get(filePath));
    this.properties = createProperties();
    loadProperties();
  }

  @Override
  public String get(final String key) {
    return properties.getProperty(key);
  }

  public Path getFilePath() {
    return filePath;
  }

  public void setProperties(final Properties properties) {
    this.properties = properties;
  }

  @Override
  public void save(final String key, final String value) {
    properties.setProperty(key, value);
    try (OutputStream output = Files.newOutputStream(filePath)) {
      properties.store(new OutputStreamWriter(output, StandardCharsets.UTF_8), null);
    } catch (final IOException e) {
      LOGGER.error("Error writing config file: {}", filePath, e);
    }
  }

  private void loadProperties() {
    createFileIfNotExists();
    try (InputStream input = Files.newInputStream(filePath)) {
      properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
    } catch (final IOException e) {
      LOGGER.error("Error reading config file: {}", filePath, e);
    }
  }

  private void createFileIfNotExists() {
    try {
      if (!Files.exists(parentDir) || !Files.exists(filePath)) {
        createFile();
      }
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
