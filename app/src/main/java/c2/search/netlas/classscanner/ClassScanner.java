package c2.search.netlas.classscanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassScanner {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClassScanner.class);
  private final List<Class<?>> classes;

  public ClassScanner(String packageName) throws IOException, ClassNotFoundException {
    classes = new ArrayList<>();
    String path = formatPath(packageName);
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream(path);
    if (inputStream == null) {
      throw new IllegalArgumentException("Package not found: " + packageName);
    }
    if (checkJar()) {
      JarInputStream jarInputStream = new JarInputStream(inputStream);
      scanClasses(jarInputStream, packageName);
    } else {
      scanClasses(inputStream, packageName);
    }
  }

  private void scanClasses(InputStream inputStream, String packageName) throws IOException {
    LOGGER.info("Scanning Dir classes in package: {}", packageName);
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    String line;
    while ((line = reader.readLine()) != null) {
      if (line.endsWith(".class")) {
        String className = packageName + "." + line.substring(0, line.length() - 6);
        addClass(className, Thread.currentThread().getContextClassLoader());
      } else {
        String subPackageName = packageName + "." + line;
        scanSubPackage(subPackageName);
      }
    }
  }

  private void scanSubPackage(String packageName) throws IOException {
    String path = formatPath(packageName);
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream(path);
    if (inputStream == null) {
      throw new IllegalArgumentException("Package not found: " + packageName);
    }
    if (path.endsWith(".jar")) {
      JarInputStream jarInputStream = new JarInputStream(inputStream);
      scanClasses(jarInputStream, packageName);
    } else {
      scanClasses(inputStream, packageName);
    }
  }

  private void scanClasses(JarInputStream jarInputStream, String packageName) throws IOException {
    LOGGER.info("Scanning Jar classes in package: {}", packageName);
    JarEntry entry;
    while ((entry = jarInputStream.getNextJarEntry()) != null) {
      LOGGER.debug("Jar entry: {}", entry.getName());
      if (entry.getName().endsWith(".class")) {
        LOGGER.info("Class entry: {}", entry.getName());
        String className = formatPath(entry.getName()).substring(0, entry.getName().length() - 6);
        addClass(className, Thread.currentThread().getContextClassLoader());
      } else if (entry.isDirectory()) {
        LOGGER.info("Dir entry: {}", entry.getName());
        String subPackageName =
            formatPath(entry.getName().substring(0, entry.getName().length() - 1));
        scanClasses(jarInputStream, subPackageName);
      }
    }
  }

  public List<Class<?>> getClasses() {
    return classes;
  }

  private String formatPath(String path) {
    return path.replace(".", "/");
  }

  protected void addClass(String className, ClassLoader loader) {
    try {
      Class<?> clazz = loader.loadClass(className);
      classes.add(clazz);
    } catch (ClassNotFoundException e) {
      LOGGER.error("Failed to load class {}", className, e);
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
    if (annotatedClasses.isEmpty()) {
      LOGGER.warn("No class annotated with {}", classAnotation);
    }
    return annotatedClasses;
  }

  private boolean checkJar() {
    Class<?> clazz = ClassScanner.class;
    ProtectionDomain protectionDomain = clazz.getProtectionDomain();
    CodeSource codeSource = protectionDomain.getCodeSource();
    LOGGER.info("Code source: {}", codeSource);
    if (codeSource == null) {
      return false;
    } else {
      URL location = codeSource.getLocation();
      LOGGER.info("File location: {}", location);
      return location.getPath().endsWith(".jar");
    }
  }
}
