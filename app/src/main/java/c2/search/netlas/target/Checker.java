package c2.search.netlas.target;

import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.annotation.Wire;
import c2.search.netlas.classscanner.ClassScanner;
import c2.search.netlas.scheme.Host;
import c2.search.netlas.scheme.Response;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Checker {
  private static final Logger LOGGER = LoggerFactory.getLogger(Checker.class);
  private static final String TARGET_CLASS_NAME = "c2.search.netlas.target";
  private NetlasWrapper netlasWrapper;
  private Host host;

  public Checker(NetlasWrapper netlasWrapper, Host host) {
    this.netlasWrapper = netlasWrapper;
    this.host = host;
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

  private void forEachMethod(Class<?> clazz, Object instant)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    for (Method method : clazz.getMethods()) {
      if (method.isAnnotationPresent(Test.class)) {
        LOGGER.info("Check method {}", method.getName());
        Response resp;
        resp = (Response) method.invoke(instant);
        System.out.println(resp);
      }
    }
  }

  private Object getInstant(Class<?> clazz)
      throws InstantiationException,
          IllegalAccessException,
          NoSuchMethodException,
          InvocationTargetException {
    LOGGER.info("Get instant {}", clazz.getName());
    return clazz.getDeclaredConstructor().newInstance();
  }

  private void forEachTarget()
      throws ClassNotFoundException,
          IOException,
          InstantiationException,
          IllegalAccessException,
          NoSuchMethodException,
          InvocationTargetException {
    var clazzes = new ClassScanner(TARGET_CLASS_NAME).getClasses();
    for (Class<?> clazz : clazzes) {
      if (clazz.isAnnotationPresent(Detect.class)) {
        var instant = getInstant(clazz);
        Detect detect = clazz.getAnnotation(Detect.class);
        System.out.println(detect.name());
        forEachField(clazz, instant);
        forEachMethod(clazz, instant);
      }
    }
  }

  public void run()
      throws ClassNotFoundException,
          InstantiationException,
          IllegalAccessException,
          NoSuchMethodException,
          InvocationTargetException,
          IOException {
    forEachTarget();
  }
}
