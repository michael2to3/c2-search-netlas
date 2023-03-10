package c2.search.netlas.annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DetectTest {
  @Test
  void testDetectAnnotation() {
    Detect detectAnnotation = MyTestClass.class.getAnnotation(Detect.class);
    assertEquals("testName", detectAnnotation.name());
  }

  @Detect(name = "testName")
  static class MyTestClass {
    // Test class with Detect annotation
  }
}
