package c2.search.netlas.annotation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TestTest {
  @Test
  void testTestAnnotation() throws NoSuchMethodException {
    Method method = MyTestClass.class.getMethod("myTest");
    assertTrue(method.isAnnotationPresent(c2.search.netlas.annotation.Test.class));
  }

  static class MyTestClass {
    @c2.search.netlas.annotation.Test
    public void myTest() {
      // Test method with Test annotation
    }
  }
}
