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
      final Field[] fields = clazz.getDeclaredFields();

      for (final Field field : fields) {
        if (field.isAnnotationPresent(Wire.class)) {
          final Object dependency = fieldValues.get(field);

          if (dependency == null) {
            LOGGER.warn("Could not find a value for field {}", field.getName());
          } else {
            inject(object, field, dependency);
          }
        }
      }

      clazz = clazz.getSuperclass();
    }
  }

  private void inject(final Object object, final Field field, final Object dependency) {
    field.setAccessible(true);
    try {
      field.set(object, dependency);
      LOGGER.info(
          "Injected dependency {} into field {} of class {}",
          dependency.getClass().getSimpleName(),
          field.getName(),
          object.getClass().getSimpleName());
    } catch (final IllegalAccessException e) {
      LOGGER.error("Failed to inject dependency into field {}", field.getName(), e);
    }
  }
}
