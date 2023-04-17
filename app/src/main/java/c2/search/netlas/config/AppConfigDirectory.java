package c2.search.netlas.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppConfigDirectory {
  public static Path getUserConfigDir(String appName) {
    String os = System.getProperty("os.name").toLowerCase();
    String userHome = System.getProperty("user.home");
    String configDir;

    if (os.contains("win")) {
      configDir = System.getenv("APPDATA");
      if (configDir == null) {
        configDir = userHome;
      }
      return Paths.get(configDir, appName);
    } else if (os.contains("mac")) {
      return Paths.get(userHome, "Library", "Application Support", appName);
    } else {
      return Paths.get(userHome, "." + appName);
    }
  }
}
