package c2.search.netlas.execute.config;

public interface ConfigManager {
  public abstract String get(String key);

  public abstract void save(String key, String value);
}
