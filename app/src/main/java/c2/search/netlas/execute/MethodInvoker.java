package c2.search.netlas.execute;

import c2.search.netlas.annotation.Test;
import c2.search.netlas.scheme.Response;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MethodInvoker {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodInvoker.class);

  private MethodInvoker() {}

  public static Response invokeTestMethod(final Method method, final Object instance) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Invoking test methods of {}", instance.getClass().getName());
    }
    Response response = null;
    final String desc = getDescriptionOfTestMethod(method);
    try {
      if (method.getReturnType() == boolean.class) {
        final boolean success = (boolean) method.invoke(instance);
        response = Response.newBuilder().setSuccess(success).setDescription(desc).build();
      } else if (method.getReturnType() == Response.class) {
        response = (Response) method.invoke(instance);
        response.setDescription(desc);
      } else {
        if (LOGGER.isErrorEnabled()) {
          LOGGER.error("Unsupported return type of test method {}", method.getName());
        }
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      MethodInvoker.handleInvocationError(method, instance, e);
    }
    return response;
  }

  public static void handleInvocationError(
      final Method method, final Object instance, final Exception exception) {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(
          "Error invoking test method {} on {} - {}",
          method.getName(),
          instance.getClass().getName(),
          exception.getMessage());
    }
  }

  public static void handleInvocationError(final Exception exception) {
    if (LOGGER.isErrorEnabled()) {
      LOGGER.error("Error invoking test methods - {}", exception.getMessage(), exception);
    }
  }

  private static String getDescriptionOfTestMethod(final Method method) {
    String description = method.getAnnotation(Test.class).description();
    if (description == null || description.isEmpty()) {
      description = method.getName();
    }
    return description;
  }
}
