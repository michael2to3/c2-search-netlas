package c2.search.netlas.classscanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class ClassScannerTest {

  @Test
  void testGetClasses() throws ClassNotFoundException, IOException {
    String packageName = "c2.search.netlas.classscanner";
    ClassScanner scanner = new ClassScanner(packageName);
    List<Class<?>> classes = scanner.getClasses();
    assertTrue(classes.size() > 0);
    assertTrue(classes.contains(MyTestClass.class));
  }

  @Test
  void testScanClasses() throws ClassNotFoundException, IOException {
    String packageName = "c2.search.netlas";
    ClassScanner scanner = new ClassScanner(packageName);
    List<Class<?>> classes = scanner.getClasses();
    assertTrue(classes.size() > 0);
  }

  static class MyTestClass {}
}
