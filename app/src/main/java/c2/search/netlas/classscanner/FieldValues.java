package c2.search.netlas.classscanner;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldValues {
  private static final Logger LOGGER = LoggerFactory.getLogger(FieldValues.class);
  private static final Map<Class<?>, Object> FIELDS = new ConcurrentHashMap<>();

  public FieldValues() {}

  public void setField(Class<?> clazz, Object value) {
    FIELDS.put(clazz, value);
  }

  public Object get(Field annotatedField) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Getting annotated field value for {}", annotatedField.getName());
    }
    return FIELDS.get(annotatedField.getType());
  }
}
