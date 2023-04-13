package c2.search.netlas.execute;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.classscanner.ClassScanner;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.Results;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Execute {
  private static final Logger LOGGER = LoggerFactory.getLogger(Execute.class);
  private static final int TIMEOUT_SECOND = 5;
  private static final String TARGET_CLASS_NAME = "c2.search.netlas.target";
  private final ClassScanner classScanner;
  private final Factory factory;

  public Execute(final FieldValues fields) {
    this.classScanner = new ClassScanner(TARGET_CLASS_NAME);
    this.factory = new Factory(fields);
  }

  public Results run() {
    final ExecutorService executor = Executors.newCachedThreadPool();
    final List<Future<Results>> futures = submitTests(executor);
    final Results results = collectResults(futures);
    executor.shutdown();
    return results;
  }

  private List<Future<Results>> submitTests(final ExecutorService executor) {
    final List<Future<Results>> futures = new ArrayList<>();
    for (final Class<?> clazz : classScanner.getClassesWithAnnotation(Detect.class)) {
      final Future<Results> future = executor.submit(() -> runTestsForClass(clazz));
      futures.add(future);
    }
    return futures;
  }

  private Results runTestsForClass(final Class<?> clazz) {
    final Object instance = factory.createInstance(clazz);
    invokeBeforeAllMethods(instance);
    final List<Response> responses = invokeTestMethods(instance);
    final Results results = new Results();
    results.addResponse(getNameOfClass(clazz), responses);
    return results;
  }

  private Results collectResults(final List<Future<Results>> futures) {
    final Results results = new Results();
    for (final var future : futures) {
      try {
        results.merge(future.get(TIMEOUT_SECOND, TimeUnit.SECONDS));
      } catch (InterruptedException | ExecutionException | TimeoutException e) {
        LOGGER.error("Error while running tests", e);
      }
    }
    return results;
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

  private List<Response> invokeTestMethods(final Object instance) {

    final ExecutorService executor = Executors.newCachedThreadPool();

    final List<Response> responses = new ArrayList<>();
    for (final Method method : MethodFinder.getTestMethods(instance.getClass())) {
      try {
        final Future<Response> feature = executor.submit(() -> MethodInvoker.invokeTestMethod(method, instance));
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        MethodInvoker.handleInvocationError(method, instance, e);
      } finally {
        executor.shutdown();
      }
    }

    return responses;
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
