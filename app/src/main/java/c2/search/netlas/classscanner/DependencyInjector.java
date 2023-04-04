package c2.search.netlas.classscanner;

import c2.search.netlas.annotation.Wire;
import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DependencyInjector {
  private static final Logger LOGGER = LoggerFactory.getLogger(DependencyInjector.class);
  private final FieldValues fieldValues;

  public DependencyInjector(final FieldValues fieldValues) {
    this.fieldValues = fieldValues;
  }

  public void inject(final Object object) {
    Class<?> clazz = object.getClass();

    while (clazz != null) {
      injectClass(object, clazz);
      clazz = clazz.getSuperclass();
    }
  }

  private void injectClass(final Object object, final Class<?> clazz) {
    final Field[] fields = clazz.getDeclaredFields();

    for (final Field field : fields) {
      if (field.isAnnotationPresent(Wire.class)) {
        injectField(object, field);
      }
    }
  }

  private void injectField(final Object object, final Field field) {
    final Object dependency = fieldValues.get(field);
    if (dependency == null) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn("Could not find a value for field {}", field.getName());
      }
    } else {
      injectFieldDep(object, field, dependency);
    }
  }

  private void injectFieldDep(final Object object, final Field field, final Object dependency) {
    final boolean isAccessible = field.canAccess(object);
    if (!isAccessible) {
      field.setAccessible(true);
    }
    try {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(
            "Injected dependency {} into field {} of class {}",
            dependency.getClass().getSimpleName(),
            field.getName(),
            object.getClass().getSimpleName());
      }
      field.set(object, dependency);
    } catch (final IllegalAccessException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Failed to inject dependency into field {}", field.getName(), e);
      }
    } finally {
      if (!isAccessible) {
        field.setAccessible(false);
      }
    }
  }

  public static Logger getLogger() {
    return LOGGER;
  }

  public FieldValues getFieldValues() {
    return fieldValues;
  }
}
