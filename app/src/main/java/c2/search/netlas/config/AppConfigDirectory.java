package c2.search.netlas.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppConfigDirectory {
  public static Path getUserConfigDir(final String appName) {
    final String os = System.getProperty("os.name").toLowerCase();
    final String userHome = System.getProperty("user.home");
    String configDir;

    Path result = Paths.get(userHome, "." + appName);
    if (os.contains("win")) {
      configDir = System.getenv("APPDATA");
      if (configDir == null) {
        configDir = userHome;
      }
      result = Paths.get(configDir, appName);
    } else if (os.contains("mac")) {
      result = Paths.get(userHome, "Library", "Application Support", appName);
    }
    return result;
  }
}
