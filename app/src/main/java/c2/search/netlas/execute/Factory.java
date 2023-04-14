package c2.search.netlas.execute;

import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Factory {
  private static final Logger LOGGER = LoggerFactory.getLogger(Execute.class);
  private final DependencyInjector dependency;

  public Factory(final FieldValues fields) {
    this.dependency = new DependencyInjector(fields);
  }

  public Object createInstance(final Class<?> clazz) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Instantiating {}", clazz.getName());
    }
    Object instant = null;
    try {
      instant = clazz.getDeclaredConstructor().newInstance();
      dependency.inject(instant);
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
}
