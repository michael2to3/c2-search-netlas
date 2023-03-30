package c2.search.netlas.classscanner;

import c2.search.netlas.annotation.BeforeAll;
import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.Results;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Checker {
  private static final Logger LOGGER = LoggerFactory.getLogger(Checker.class);
  private static final String TARGET_CLASS_NAME = "c2.search.netlas.target";
  private ClassScanner classScanner;
  private AnnotatedFieldValues fields;

  public Checker(AnnotatedFieldValues fields) throws ClassNotFoundException, IOException {
    this.classScanner = new ClassScanner(TARGET_CLASS_NAME);
    this.fields = fields;
  }

  public ClassScanner getClassScanner() {
    return classScanner;
  }

  public void setClassScanner(ClassScanner classScanner) {
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
    List<Class<?>> detectedClasses = classScanner.getClassesWithAnnotation(Detect.class);
    if (detectedClasses.isEmpty()) {
      throw new IllegalStateException(
          "No class with @Detect annotation found in " + TARGET_CLASS_NAME);
    }
    Results results = new Results();
    for (Class<?> clazz : detectedClasses) {
      Object instant = instantiateClass(clazz);
      injectDependencies(instant);
      invokeBeforeAllMethods(instant);
      results.addResponse(getNameOfClass(clazz), invokeTestMethods(instant));
    }
    return results;
  }

  private Object instantiateClass(Class<?> clazz)
      throws InstantiationException,
          IllegalAccessException,
          IllegalArgumentException,
          InvocationTargetException,
          NoSuchMethodException,
          SecurityException {
    LOGGER.info("Instantiating {}", clazz.getName());
    return clazz.getDeclaredConstructor().newInstance();
  }

  private String getNameOfClass(Class<?> clazz) {
    Detect detect = clazz.getAnnotation(Detect.class);
    String nameOfDetect = detect.name();
    if (nameOfDetect == null || nameOfDetect.isEmpty()) {
      nameOfDetect = clazz.getName();
    }
    return nameOfDetect;
  }

  private List<Response> invokeTestMethods(Object instant) {
    List<Response> responses = new ArrayList<>();
    for (Method method : getTestMethods(instant.getClass())) {
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

  private List<Method> getTestMethods(Class<?> clazz) {
    List<Method> testMethods = new ArrayList<>();
    for (Method method : clazz.getMethods()) {
      if (method.isAnnotationPresent(Test.class)) {
        testMethods.add(method);
      }
    }
    return testMethods;
  }

  private String getDescriptionOfTestMethod(Method method) {
    String description = method.getAnnotation(Test.class).description();
    if (description == null || description.isEmpty()) {
      description = method.getName();
    }
    return description;
  }

  private Response invokeTestMethod(Method method, Object instant)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    LOGGER.info("Invoking test methods of {}", instant.getClass().getName());
    Response response = (Response) method.invoke(instant);
    response.setDescription(getDescriptionOfTestMethod(method));
    return response;
  }

  private void handleInvocationError(Method method, Object instant, Exception e) {
    LOGGER.info(
        "Error invoking test method {} on {} - {}",
        method.getName(),
        instant.getClass().getName(),
        e.getMessage());
  }

  private void injectDependencies(Object instant)
      throws IllegalArgumentException, IllegalAccessException {
    LOGGER.info("Injecting dependencies to {}", instant.getClass().getName());
    Field[] annotatedFields = instant.getClass().getDeclaredFields();
    for (Field annotatedField : annotatedFields) {
      if (!annotatedField.isAnnotationPresent(Wire.class)) {
        continue;
      }
      annotatedField.setAccessible(true);
      Object value = fields.get(annotatedField);
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

  private static Socket getSocket(Host host, int timeout) {
    LOGGER.info("Getting socket");
    Socket socket = null;
    try {
      socket = new Socket(host.getTarget(), host.getPort());
      socket.setSoTimeout(timeout);
    } catch (IOException e) {
      LOGGER.error("Failed to connect to {}:{}", host.getTarget(), host.getPort());
    }
    return socket;
  }

  private List<Method> getBeforeAllMethods(Class<?> clazz) {
    List<Method> beforeAllMethods = new ArrayList<>();
    for (Method method : clazz.getMethods()) {
      if (method.isAnnotationPresent(BeforeAll.class)) {
        beforeAllMethods.add(method);
      }
    }
    return beforeAllMethods;
  }

  private void invokeBeforeAllMethods(Object instant)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    LOGGER.info("Invoking beforeAll methods of {}", instant.getClass().getName());
    List<Method> beforeAllMethods = getBeforeAllMethods(instant.getClass());
    for (Method method : beforeAllMethods) {
      method.invoke(instant);
    }
  }
}
