package c2.search.netlas.classscanner;

import c2.search.netlas.annotation.BeforeAll;
import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.Results;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Checker {
  private static final Logger LOGGER = LoggerFactory.getLogger(Checker.class);
  private static final String TARGET_CLASS_NAME = "c2.search.netlas.target";
  private ClassScanner classScanner;
  private final FieldValues fields;

  public Checker(final FieldValues fields) throws ClassNotFoundException, IOException {
    this.classScanner = new ClassScanner(TARGET_CLASS_NAME);
    this.fields = fields;
  }

  public ClassScanner getClassScanner() {
    return classScanner;
  }

  public void setClassScanner(final ClassScanner classScanner) {
    this.classScanner = classScanner;
  }

  public static Logger getLogger() {
    return LOGGER;
  }

  public static String getTargetClassName() {
    return TARGET_CLASS_NAME;
  }

  public Results run()
      throws IllegalAccessException,
          InstantiationException,
          InvocationTargetException,
          NoSuchMethodException,
          SecurityException {
    final List<Class<?>> detectedClasses = classScanner.getClassesWithAnnotation(Detect.class);
    if (detectedClasses.isEmpty()) {
      throw new IllegalStateException(
          "No class with @Detect annotation found in " + TARGET_CLASS_NAME);
    }
    final Results results = new Results();
    for (final Class<?> clazz : detectedClasses) {
      final Object instant = instantiateClass(clazz);
      injectDependencies(instant);
      invokeBeforeAllMethods(instant);
      results.addResponse(getNameOfClass(clazz), invokeTestMethods(instant));
    }
    return results;
  }

  private Object instantiateClass(final Class<?> clazz)
      throws InstantiationException,
          IllegalAccessException,
          IllegalArgumentException,
          InvocationTargetException,
          NoSuchMethodException,
          SecurityException {
    LOGGER.info("Instantiating {}", clazz.getName());
    return clazz.getDeclaredConstructor().newInstance();
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
      Response response = new Response(false);
      try {
        response = invokeTestMethod(method, instant);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        handleInvocationError(method, instant, e);
      }
      responses.add(response);
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
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    LOGGER.info("Invoking test methods of {}", instant.getClass().getName());
    final Response response = (Response) method.invoke(instant);
    response.setDescription(getDescriptionOfTestMethod(method));
    return response;
  }

  private void handleInvocationError(final Method method, final Object instant, final Exception e) {
    LOGGER.info(
        "Error invoking test method {} on {} - {}",
        method.getName(),
        instant.getClass().getName(),
        e.getMessage());
  }

  private void injectDependencies(final Object instant)
      throws IllegalArgumentException, IllegalAccessException {
    LOGGER.info("Injecting dependencies to {}", instant.getClass().getName());
    final Field[] annotatedFields = instant.getClass().getDeclaredFields();
    for (final Field annotatedField : annotatedFields) {
      if (!annotatedField.isAnnotationPresent(Wire.class)) {
        continue;
      }
      annotatedField.setAccessible(true);
      final Object value = fields.get(annotatedField);
      if (value == null) {
        LOGGER.warn(
            "Unrecognized field {} in {}", annotatedField.getType(), instant.getClass().getName());
        continue;
      }
      annotatedField.set(instant, value);
      LOGGER.info(
          "Injected dependency {} to {}",
          annotatedField.getType().getSimpleName(),
          instant.getClass().getSimpleName());
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

  private void invokeBeforeAllMethods(final Object instant)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    LOGGER.info("Invoking beforeAll methods of {}", instant.getClass().getName());
    final List<Method> beforeAllMethods = getBeforeAllMethods(instant.getClass());
    for (final Method method : beforeAllMethods) {
      method.invoke(instant);
    }
  }
}
