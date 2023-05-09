package c2.search.netlas.execute;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.classscanner.ClassScanner;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.Results;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DetectSubmit implements Submit {
  private static final Logger LOGGER = LoggerFactory.getLogger(Execute.class);
  private static final int TIMEOUT_SINGLE = 5;
  private final ClassScanner classScanner;
  private final Factory factory;

  public DetectSubmit(final ClassScanner classScanner, final Factory factory) {
    this.classScanner = classScanner;
    this.factory = factory;
  }

  @Override
  public List<Future<Results>> submitTests(final ExecutorService executor) {
    return classScanner.getClassesWithAnnotation(Detect.class).stream()
        .map(clazz -> executor.submit(() -> runTestsForClass(clazz)))
        .collect(Collectors.toList());
  }

  private Results runTestsForClass(final Class<?> clazz) {
    final Object instance = factory.createInstance(clazz);
    invokeBeforeAllMethods(instance);
    final List<CompletableFuture<Response>> futures = invokeTestMethods(instance);
    return createResultsFromFutures(clazz, futures);
  }

  private void invokeBeforeAllMethods(final Object instance) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Invoking beforeAll methods of {}", instance.getClass().getName());
    }
    final List<Method> beforeAllMethods = MethodFinder.getBeforeAllMethods(instance.getClass());
    for (final Method method : beforeAllMethods) {
      try {
        method.invoke(instance);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        MethodInvoker.handleInvocationError(method, instance, e);
      }
    }
  }

  private List<CompletableFuture<Response>> invokeTestMethods(final Object instance) {
    final List<CompletableFuture<Response>> futures = new ArrayList<>();
    for (final Method method : MethodFinder.getTestMethods(instance.getClass())) {
      final CompletableFuture<Response> future =
          CompletableFuture.supplyAsync(() -> MethodInvoker.invokeTestMethod(method, instance));
      futures.add(future);
    }
    return futures;
  }

  private Results createResultsFromFutures(
      final Class<?> clazz, final List<CompletableFuture<Response>> futures) {
    final List<Response> responses = new ArrayList<>();
    for (final CompletableFuture<Response> future : futures) {
      try {
        final Response response = future.get(TIMEOUT_SINGLE, TimeUnit.SECONDS);
        if (response != null) {
          responses.add(response);
        }
      } catch (final TimeoutException e) {
        LOGGER.info("Timed out after {} seconds", TIMEOUT_SINGLE, e);
      } catch (InterruptedException | ExecutionException e) {
        MethodInvoker.handleInvocationError(e);
      }
    }
    final Results results = new Results();
    results.addResponse(getNameOfClass(clazz), responses);
    return results;
  }

  private String getNameOfClass(final Class<?> clazz) {
    final Detect detect = clazz.getAnnotation(Detect.class);
    String nameOfDetect = detect.name();
    if (nameOfDetect == null || nameOfDetect.isEmpty()) {
      nameOfDetect = clazz.getName();
    }
    return nameOfDetect;
  }
}
