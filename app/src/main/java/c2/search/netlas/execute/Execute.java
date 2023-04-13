package c2.search.netlas.execute;

import c2.search.netlas.annotation.BeforeAll;
import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.classscanner.ClassScanner;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.ResponseBuilder;
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
  private ClassScanner classScanner;
  private final Factory factory;

  public Execute(FieldValues fields) {
    this.classScanner = new ClassScanner(TARGET_CLASS_NAME);
    this.factory = new Factory(fields);
  }

  public Results run() {
    ExecutorService executor = Executors.newCachedThreadPool();
    List<Future<Results>> futures = submitTests(executor);
    Results results = collectResults(futures);
    executor.shutdown();
    return results;
  }

  private List<Future<Results>> submitTests(ExecutorService executor) {
    List<Future<Results>> futures = new ArrayList<>();
    for (Class<?> clazz : classScanner.getClassesWithAnnotation(Detect.class)) {
      Future<Results> future = executor.submit(() -> runTestsForClass(clazz));
      futures.add(future);
    }
    return futures;
  }

  private Results runTestsForClass(Class<?> clazz) {
    Object instance = factory.createInstance(clazz);
    invokeBeforeAllMethods(instance);
    List<Response> responses = invokeTestMethods(instance);
    Results results = new Results();
    results.addResponse(getNameOfClass(clazz), responses);
    return results;
  }

  private Results collectResults(List<Future<Results>> futures) {
    Results results = new Results();
    for (var future : futures) {
      try {
        results.merge(future.get(TIMEOUT_SECOND, TimeUnit.SECONDS));
      } catch (InterruptedException | ExecutionException | TimeoutException e) {
        LOGGER.error("Error while running tests", e);
      }
    }
    return results;
  }

  private List<Method> getBeforeAllMethods(final Class<?> clazz) {
    final List<Method> beforeAllMethods = new ArrayList<>();
    for (final Method method : clazz.getMethods()) {
      if (method.isAnnotationPresent(BeforeAll.class)) {
        beforeAllMethods.add(method);
      }
    }
    return beforeAllMethods;
  }

  private void invokeBeforeAllMethods(Object instance) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Invoking beforeAll methods of {}", instance.getClass().getName());
    }
    final List<Method> beforeAllMethods = getBeforeAllMethods(instance.getClass());
    for (final Method method : beforeAllMethods) {
      try {
        method.invoke(instance);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        handleInvocationError(method, instance, e);
      }
    }
  }

  private List<Response> invokeTestMethods(Object instance) {
    final List<Response> responses = new ArrayList<>();
    for (final Method method : getTestMethods(instance.getClass())) {
      try {
        responses.add(invokeTestMethod(method, instance));
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        handleInvocationError(method, instance, e);
      }
    }

    return responses;
  }

  private List<Method> getTestMethods(final Class<?> clazz) {
    final List<Method> testMethods = new ArrayList<>();
    for (final Method method : clazz.getMethods()) {
      if (method.isAnnotationPresent(Test.class)) {
        testMethods.add(method);
      }
    }
    return testMethods;
  }

  private String getDescriptionOfTestMethod(final Method method) {
    String description = method.getAnnotation(Test.class).description();
    if (description == null || description.isEmpty()) {
      description = method.getName();
    }
    return description;
  }

  private Response invokeTestMethod(final Method method, final Object instant)
      throws IllegalAccessException, InvocationTargetException {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Invoking test methods of {}", instant.getClass().getName());
    }
    Response response = null;
    final String desc = getDescriptionOfTestMethod(method);
    if (method.getReturnType() == boolean.class) {
      final boolean success = (boolean) method.invoke(instant);
      response = new ResponseBuilder().setSuccess(success).setDescription(desc).build();
    } else if (method.getReturnType() == Response.class) {
      response = (Response) method.invoke(instant);
      response.setDescription(desc);
    } else {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Unsupported return type of test method {}", method.getName());
      }
    }
    return response;
  }

  private void handleInvocationError(
      final Method method, final Object instant, final Exception exception) {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(
          "Error invoking test method {} on {} - {}",
          method.getName(),
          instant.getClass().getName(),
          exception.getMessage());
    }
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
