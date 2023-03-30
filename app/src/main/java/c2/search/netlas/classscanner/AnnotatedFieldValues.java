package c2.search.netlas.classscanner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotatedFieldValues {
  private static final Logger LOGGER = LoggerFactory.getLogger(AnnotatedFieldValues.class);
  private static final Map<Class<?>, Object> FIELDS = new HashMap<>();

  public AnnotatedFieldValues() {}

  public void setField(Class<?> clazz, Object value) {
    FIELDS.put(clazz, value);
  }

  public Object get(Field annotatedField) {
    LOGGER.debug("Getting annotated field value for {}", annotatedField.getName());
    return FIELDS.get(annotatedField.getType());
  }
}
