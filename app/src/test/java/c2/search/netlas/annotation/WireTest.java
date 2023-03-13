package c2.search.netlas.annotation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WireTest {

  @Test
  void testWireAnnotation() throws NoSuchFieldException {
    Wire wireAnnotation = MyTestClass.class.getDeclaredField("myField").getAnnotation(Wire.class);
    assertTrue(wireAnnotation != null);
  }

  static class MyTestClass {
    @Wire private Object myField;
  }
}
