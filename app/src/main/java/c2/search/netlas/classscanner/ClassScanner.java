package c2.search.netlas.classscanner;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {

  private final List<Class<?>> classes;

  public ClassScanner(String packageName) throws IOException, ClassNotFoundException {
    classes = new ArrayList<>();
    String path = packageName.replace(".", "/");
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    for (File file : new File(classLoader.getResource(path).getFile()).listFiles()) {
      if (file.isDirectory()) {
        scanClasses(packageName + "." + file.getName());
      } else if (file.getName().endsWith(".class")) {
        String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
        Class<?> clazz = classLoader.loadClass(className);
        classes.add(clazz);
      }
    }
  }

  public List<Class<?>> getClasses() {
    return classes;
  }

  private void scanClasses(String packageName) throws ClassNotFoundException, IOException {
    String path = packageName.replace(".", "/");
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    for (File file : new File(classLoader.getResource(path).getFile()).listFiles()) {
      if (file.isDirectory()) {
        scanClasses(packageName + "." + file.getName());
      } else if (file.getName().endsWith(".class")) {
        String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
        Class<?> clazz = classLoader.loadClass(className);
        classes.add(clazz);
      }
    }
  }

  public List<Class<?>> getClassesWithAnnotation(Class<? extends Annotation> classAnotation) {
    List<Class<?>> annotatedClasses = new ArrayList<>();
    for (Class<?> clazz : classes) {
      Annotation[] classAnnotations = clazz.getAnnotations();
      for (Annotation annotation : classAnnotations) {
        if (annotation.annotationType() == classAnotation) {
          annotatedClasses.add(clazz);
          break;
        }
      }
    }
    return annotatedClasses;
  }
}
