package c2.search.netlas.target.deimos;

import c2.search.netlas.scheme.Host;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Utils {
  private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
  private static final int TIMEOUT = 5000;

  private Utils() {}

  public static String[] makeHttpRequest(final Host host, final String path) throws IOException {
    final OkHttpClient client =
        new OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .build();

    HttpUrl url =
        new HttpUrl.Builder()
            .scheme("https")
            .host(host.getTarget())
            .port(host.getPort())
            .addPathSegments(path)
            .build();

    Request request = new Request.Builder().url(url).get().build();

    Response response = null;
    boolean useHttps = true;
    final List<String> results = new ArrayList<>();

    try {
      response = client.newCall(request).execute();
      results.add(String.valueOf(response.code()));
      final ResponseBody responseBody = response.body();
      if (responseBody != null) {
        final String responseString = responseBody.string();
        results.addAll(Arrays.asList(responseString.split("\\r?\\n")));
      }
    } catch (final IOException e) {
      useHttps = false;
    } finally {
      if (response != null) {
        response.close();
      }
    }

    if (!useHttps) {
      url = url.newBuilder().scheme("http").build();
      request = new Request.Builder().url(url).get().build();

      try {
        response = client.newCall(request).execute();
        results.add(String.valueOf(response.code()));
        final ResponseBody responseBody = response.body();
        if (responseBody != null) {
          final String responseString = responseBody.string();
          results.addAll(Arrays.asList(responseString.split("\\r?\\n")));
        }
      } catch (final IOException e) {
        if (LOGGER.isWarnEnabled()) {
          LOGGER.warn("Could not connect to " + host.getTarget() + ":" + host.getPort() + path, e);
        }
      } finally {
        if (response != null) {
          response.close();
        }
      }
    }

    return results.toArray(new String[0]);
  }
}
