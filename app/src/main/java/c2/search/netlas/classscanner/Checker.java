package c2.search.netlas.classscanner;

import c2.search.netlas.annotation.BeforeAll;
import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.ResponseBuilder;
import c2.search.netlas.scheme.Results;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Checker {
  private static final Logger LOGGER = LoggerFactory.getLogger(Checker.class);
  private static final String TARGET_CLASS_NAME = "c2.search.netlas.target";
  private ClassScanner classScanner;
  private final DependencyInjector depInjector;

  public Checker(final FieldValues fields) {
    this.classScanner = new ClassScanner(TARGET_CLASS_NAME);
    this.depInjector = new DependencyInjector(fields);
  }

  public void setClassScanner(final ClassScanner classScanner) {
    this.classScanner = classScanner;
  }

  public Results run() {
    final List<Class<?>> detectedClasses = classScanner.getClassesWithAnnotation(Detect.class);
    final Results results = new Results();
    final List<CompletableFuture<Void>> futures = new ArrayList<>();
    for (final Class<?> clazz : detectedClasses) {
      final Object instant = instantiateClass(clazz);
      depInjector.inject(instant);
      final CompletableFuture<Void> future = CompletableFuture.runAsync(
          () -> {
            invokeBeforeAllMethods(instant);
            results.addResponse(getNameOfClass(clazz), invokeTestMethods(instant));
          });
      futures.add(future);
    }
    try {
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
    } catch (InterruptedException | ExecutionException e) {
      LOGGER.error("Error while running tests", e);
    }
    return results;
  }

  private Object instantiateClass(final Class<?> clazz) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Instantiating {}", clazz.getName());
    }
    Object instant = null;
    try {
      instant = clazz.getDeclaredConstructor().newInstance();
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Failed to instantiate {}", clazz.getName(), e);
      }
    }
    return instant;
  }

  private String getNameOfClass(final Class<?> clazz) {
    final Detect detect = clazz.getAnnotation(Detect.class);
    String nameOfDetect = detect.name();
    if (nameOfDetect == null || nameOfDetect.isEmpty()) {
      nameOfDetect = clazz.getName();
    }
    return nameOfDetect;
  }

  private List<Response> invokeTestMethods(final Object instant) {
    final List<Response> responses = new ArrayList<>();
    for (final Method method : getTestMethods(instant.getClass())) {
      try {
        responses.add(invokeTestMethod(method, instant));
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        handleInvocationError(method, instant, e);
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

  private List<Method> getBeforeAllMethods(final Class<?> clazz) {
    final List<Method> beforeAllMethods = new ArrayList<>();
    for (final Method method : clazz.getMethods()) {
      if (method.isAnnotationPresent(BeforeAll.class)) {
        beforeAllMethods.add(method);
      }
    }
    return beforeAllMethods;
  }

  private void invokeBeforeAllMethods(final Object instant) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Invoking beforeAll methods of {}", instant.getClass().getName());
    }
    final List<Method> beforeAllMethods = getBeforeAllMethods(instant.getClass());
    for (final Method method : beforeAllMethods) {
      try {
        method.invoke(instant);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        handleInvocationError(method, instant, e);
      }
    }
  }
}
