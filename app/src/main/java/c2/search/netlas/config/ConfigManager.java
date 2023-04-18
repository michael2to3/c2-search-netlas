package c2.search.netlas.config;

public interface ConfigManager {
  String get(String key);

  void save(String key, String value);
}
