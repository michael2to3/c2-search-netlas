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

  public ClassScanner(final String packageName) {
    classes = new ArrayList<>();
    try (ScanResult scanResult =
        new ClassGraph().enableAllInfo().acceptPackages(packageName).scan()) {
      scanResult.getAllClasses().loadClasses().forEach(this::addClass);
    }
  }

  protected void addClass(final Class<?> clazz) {
    classes.add(clazz);
  }

  public List<Class<?>> getClasses() {
    return classes;
  }

  public List<Class<?>> getClassesWithAnnotation(
      final Class<? extends Annotation> classAnnotation) {
    final List<Class<?>> annotatedClasses = new ArrayList<>();
    for (final Class<?> clazz : classes) {
      final Annotation[] classAnnotations = clazz.getAnnotations();
      for (final Annotation annotation : classAnnotations) {
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
