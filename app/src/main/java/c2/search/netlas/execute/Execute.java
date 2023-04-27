package c2.search.netlas.execute;

import c2.search.netlas.classscanner.ClassScanner;
import c2.search.netlas.scheme.Results;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import netlas.java.scheme.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Execute {
  private static final Logger LOGGER = LoggerFactory.getLogger(Execute.class);
  private static final int TIMEOUT_COMPLEX = 120;
  private static final String TARGET_CLASS_NAME = "c2.search.netlas.target";
  private final ClassScanner classScanner;
  private final Factory factory;
  private final Data data;

  public Execute(final FieldValues fields, final Data data) {
    this.classScanner = new ClassScanner(TARGET_CLASS_NAME);
    this.factory = new Factory(fields);
    this.data = data;
  }

  public Results run() {
    final ExecutorService executor = createExecutorService();
    final List<Future<Results>> futures = new ArrayList<>();
    final Submit detect = new DetectSubmit(classScanner, factory);
    final Submit staticSubmit = new StaticSubmit(classScanner, data);
    futures.addAll(detect.submitTests(executor));
    futures.addAll(staticSubmit.submitTests(executor));
    final Results results = collectResults(futures);
    executor.shutdown();
    return results;
  }

  private ExecutorService createExecutorService() {
    final int processors = Runtime.getRuntime().availableProcessors();
    final int numberOfThreads = Math.max(2, processors - 1);
    return Executors.newFixedThreadPool(numberOfThreads);
  }

  private Results collectResults(final List<Future<Results>> futures) {
    final Results results = new Results();
    for (final var future : futures) {
      try {
        results.merge(future.get(TIMEOUT_COMPLEX, TimeUnit.SECONDS));
      } catch (TimeoutException e) {
        LOGGER.warn("Skipping test due to timeout", e);
      } catch (InterruptedException | ExecutionException e) {
        LOGGER.error("Error while running tests", e);
      }
    }
    return results;
  }
}
