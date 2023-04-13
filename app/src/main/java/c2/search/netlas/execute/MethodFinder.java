package c2.search.netlas.execute;

import c2.search.netlas.annotation.BeforeAll;
import c2.search.netlas.annotation.Test;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodFinder {
  public static List<Method> getBeforeAllMethods(final Class<?> clazz) {
    final List<Method> beforeAllMethods = new ArrayList<>();
    for (final Method method : clazz.getMethods()) {
      if (method.isAnnotationPresent(BeforeAll.class)) {
        beforeAllMethods.add(method);
      }
    }
    return beforeAllMethods;
  }

  public static List<Method> getTestMethods(final Class<?> clazz) {
    final List<Method> testMethods = new ArrayList<>();
    for (final Method method : clazz.getMethods()) {
      if (method.isAnnotationPresent(Test.class)) {
        testMethods.add(method);
      }
    }
    return testMethods;
  }
}
