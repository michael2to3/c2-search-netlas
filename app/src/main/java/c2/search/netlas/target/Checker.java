package c2.search.netlas.target;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.classscanner.ClassScanner;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.Results;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import netlas.java.Netlas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Checker {
  private static final Logger LOGGER = LoggerFactory.getLogger(Checker.class);
  private static final String TARGET_CLASS_NAME = "c2.search.netlas.target";
  private NetlasWrapper netlasWrapper;
  private Host host;
  private ClassScanner classScanner;

  public NetlasWrapper getNetlasWrapper() {
    return netlasWrapper;
  }

  public void setNetlasWrapper(NetlasWrapper netlasWrapper) {
    this.netlasWrapper = netlasWrapper;
  }

  public Host getHost() {
    return host;
  }

  public void setHost(Host host) {
    this.host = host;
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

  public Checker(NetlasWrapper netlasWrapper, Host host)
      throws ClassNotFoundException, IOException {
    this.netlasWrapper = netlasWrapper;
    this.host = host;
    this.classScanner = new ClassScanner(TARGET_CLASS_NAME);
  }

  public Results run()
      throws ClassNotFoundException,
          InstantiationException,
          IllegalAccessException,
          NoSuchMethodException,
          InvocationTargetException,
          IOException {
    List<Class<?>> detectedClasses = classScanner.getClassesWithAnnotation(Detect.class);
    if (detectedClasses.isEmpty()) {
      throw new IllegalStateException(
          "No class with @Detect annotation found in " + TARGET_CLASS_NAME);
    }
    Results results = new Results();
    for (Class<?> clazz : detectedClasses) {
      Object instant = instantiateClass(clazz);
      injectDependencies(instant);
      results.addResponse(getNameOfClass(clazz), invokeTestMethods(instant));
    }
    return results;
  }

  private Object instantiateClass(Class<?> clazz)
      throws IllegalAccessException,
          InstantiationException,
          NoSuchMethodException,
          InvocationTargetException {
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
    LOGGER.info("Invoking test methods of {}", instant.getClass().getName());
    List<Response> responses = new ArrayList<>();
    for (Method method : getTestMethods(instant.getClass())) {
      try {
        Response response = invokeTestMethod(method, instant);
        responses.add(response);
      } catch (Exception e) {
        handleInvocationError(method, instant, e);
        responses.add(new Response(false));
      }
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

  private Response invokeTestMethod(Method method, Object instant) throws Exception {
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

  private void injectDependencies(Object instant) throws IllegalAccessException {
    LOGGER.info("Injecting dependencies to {}", instant.getClass().getName());
    Field[] annotatedFields = instant.getClass().getDeclaredFields();
    for (Field annotatedField : annotatedFields) {
      if (!annotatedField.isAnnotationPresent(Wire.class)) {
        continue;
      }
      annotatedField.setAccessible(true);
      Object value = getAnnotatedFieldValue(annotatedField);
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

  private Object getAnnotatedFieldValue(Field annotatedField) {
    Class<?> typeOfVariable = annotatedField.getType();
    if (typeOfVariable.equals(NetlasWrapper.class)) {
      return this.netlasWrapper;
    } else if (typeOfVariable.equals(Host.class)) {
      return this.host;
    } else if (typeOfVariable.equals(Netlas.class)) {
      return this.netlasWrapper.getNetlas();
    }
    return null;
  }
}
