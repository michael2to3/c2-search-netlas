package c2.search.netlas.classscanner;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassScanner {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClassScanner.class);
  private final List<Class<?>> classes;

  public ClassScanner(String packageName) {
    classes = new ArrayList<>();
    try (ScanResult scanResult =
        new ClassGraph().enableAllInfo().whitelistPackages(packageName).scan()) {
      scanResult.getAllClasses().loadClasses().forEach(this::addClass);
    }
  }

  protected void addClass(Class<?> clazz) {
    classes.add(clazz);
  }

  public List<Class<?>> getClasses() {
    return classes;
  }

  public List<Class<?>> getClassesWithAnnotation(Class<? extends Annotation> classAnnotation) {
    List<Class<?>> annotatedClasses = new ArrayList<>();
    for (Class<?> clazz : classes) {
      Annotation[] classAnnotations = clazz.getAnnotations();
      for (Annotation annotation : classAnnotations) {
        if (annotation.annotationType() == classAnnotation) {
          annotatedClasses.add(clazz);
          break;
        }
      }
    }
    if (annotatedClasses.isEmpty()) {
      LOGGER.warn("No class annotated with {}", classAnnotation);
    }
    return annotatedClasses;
  }
}
