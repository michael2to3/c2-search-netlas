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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Checker {
  private static final Logger LOGGER = LoggerFactory.getLogger(Checker.class);
  private static final String TARGET_CLASS_NAME = "c2.search.netlas.target";
  private NetlasWrapper netlasWrapper;
  private Host host;
  private ClassScanner classScanner;

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
    return forEachTarget();
  }

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

  private void changeField(Field field, Object instant) {
    Wire wire = field.getAnnotation(Wire.class);
    String nameOfVariable = wire.name();
    if (nameOfVariable == null || nameOfVariable.isEmpty()) {
      nameOfVariable = field.getName();
    }
    LOGGER.info("Change field {} to {}", nameOfVariable, instant);
    field.setAccessible(true);
    try {
      switch (nameOfVariable) {
        case "netlasWrapper":
          field.set(instant, this.netlasWrapper);
          break;
        case "netlas":
          field.set(instant, this.netlasWrapper.getNetlas());
          break;
        case "host":
          field.set(instant, this.host);
          break;
      }
    } catch (IllegalAccessException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  private void forEachField(Class<?> clazz, Object instant) {
    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(Wire.class)) {
        changeField(field, instant);
      }
    }
  }

  private Response invokeMethod(Method method, Object instant)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    if (method.isAnnotationPresent(Test.class)) {
      LOGGER.info("Check method {}", method.getName());
      return (Response) method.invoke(instant);
    }
    return null;
  }

  private List<Response> forEachMethod(Class<?> clazz, Object instant)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    List<Response> responses = new ArrayList<>();
    Response resp = null;
    for (Method method : clazz.getMethods()) {
      try {
        resp = invokeMethod(method, instant);
      } catch (Exception e) {
        LOGGER.info(e.getMessage(), e);
      }
      if (resp != null) {
        responses.add(resp);
      }
    }
    return responses;
  }

  private Object getInstant(Class<?> clazz)
      throws InstantiationException,
          IllegalAccessException,
          NoSuchMethodException,
          InvocationTargetException {
    LOGGER.info("Get instant {}", clazz.getName());
    return clazz.getDeclaredConstructor().newInstance();
  }

  private Results forEachTarget()
      throws ClassNotFoundException,
          IOException,
          InstantiationException,
          IllegalAccessException,
          NoSuchMethodException,
          InvocationTargetException {
    var clazzes = this.classScanner.getClasses();
    if (clazzes.isEmpty()) {
      throw new IllegalArgumentException("No class found");
    }
    Results reponses = new Results();
    for (Class<?> clazz : clazzes) {
      if (clazz.isAnnotationPresent(Detect.class)) {
        var instant = getInstant(clazz);
        Detect detect = clazz.getAnnotation(Detect.class);
        String nameOfDetect = detect.name();
        if (nameOfDetect == null || nameOfDetect.isEmpty()) {
          nameOfDetect = clazz.getName();
        }
        LOGGER.info("Detect {}", nameOfDetect);

        forEachField(clazz, instant);
        reponses.addResponse(detect.name(), forEachMethod(clazz, instant));
      }
    }
    return reponses;
  }
}
