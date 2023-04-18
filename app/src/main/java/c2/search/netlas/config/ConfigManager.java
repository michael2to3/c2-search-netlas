package c2.search.netlas.config;

public interface ConfigManager {
  abstract String get(String key);

  abstract void save(String key, String value);
}
