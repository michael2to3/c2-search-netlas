package c2.search.netlas.execute;

import c2.search.netlas.annotation.Test;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.ResponseBuilder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodInvoker {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodInvoker.class);

  public static Response invokeTestMethod(final Method method, final Object instance)
      throws IllegalAccessException, InvocationTargetException {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Invoking test methods of {}", instance.getClass().getName());
    }
    Response response = null;
    final String desc = getDescriptionOfTestMethod(method);
    if (method.getReturnType() == boolean.class) {
      final boolean success = (boolean) method.invoke(instance);
      response = new ResponseBuilder().setSuccess(success).setDescription(desc).build();
    } else if (method.getReturnType() == Response.class) {
      response = (Response) method.invoke(instance);
      response.setDescription(desc);
    } else {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Unsupported return type of test method {}", method.getName());
      }
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

  private static String getDescriptionOfTestMethod(final Method method) {
    String description = method.getAnnotation(Test.class).description();
    if (description == null || description.isEmpty()) {
      description = method.getName();
    }
    return description;
  }
}
