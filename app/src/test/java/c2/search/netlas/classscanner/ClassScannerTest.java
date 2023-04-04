package c2.search.netlas.classscanner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import c2.search.netlas.annotation.Detect;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.jar.JarInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ClassScannerTest {
  @BeforeEach
  public void setup() throws Exception {
    MockitoAnnotations.openMocks(this);
    classScanner = spy(new ClassScanner("c2.search.netlas"));
  }

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

  @Test
  void testScanClassesWithAnnotation() throws ClassNotFoundException, IOException {
    String packageName = "c2.search.netlas";
    ClassScanner scanner = new ClassScanner(packageName);
    List<Class<?>> classes = scanner.getClassesWithAnnotation(Detect.class);
    assertTrue(classes.size() > 0);
  }

  @Mock private InputStream inputStream;

  @Mock private JarInputStream jarInputStream;

  private ClassScanner classScanner;

  @Test
  public void testScanClassesWithJar() throws IOException {
    doReturn(null).when(jarInputStream).getNextJarEntry();
    doReturn(true).when(classScanner).checkJar();
    doNothing().when(classScanner).scanClasses(any(), anyString());
    classScanner.scanClasses(jarInputStream, "c2.search.netlas");
    verify(classScanner, times(1)).scanClasses(any(), anyString());
  }

  static class MyTestClass {}
}
