package c2.search.netlas.execute;

import c2.search.netlas.classscanner.FieldValues;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Factory {
  private static final Logger LOGGER = LoggerFactory.getLogger(Execute.class);
  private final FieldValues fields;

  public Factory(final FieldValues fields) {
    this.fields = fields;
  }

  public Object createInstance(final Class<?> clazz) {
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
}
