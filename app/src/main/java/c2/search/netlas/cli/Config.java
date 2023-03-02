package c2.search.netlas.cli;

import java.io.*;
import java.util.Properties;

public class Config {
    private String api;
    private final String configFile = "/path/to/config/file.properties";

    public String getApi() throws IOException {
        if (api == null) {
            load();
        }
        return api;
    }

    public void setApi(String api) throws IOException {
        this.api = api;
        save();
    }

    private void save() throws IOException {
        Properties properties = new Properties();
        properties.setProperty("api", api);

        try (OutputStream outputStream = new FileOutputStream(configFile)) {
            properties.store(outputStream, "API Configuration");
        }
    }

    private void load() throws IOException {
        File file = new File(configFile);
        if (file.exists()) {
            Properties properties = new Properties();
            try (InputStream inputStream = new FileInputStream(file)) {
                properties.load(inputStream);
            }
            api = properties.getProperty("api");
        }
    }
}
