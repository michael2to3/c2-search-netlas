package c2.search.netlas.execute;

import c2.search.netlas.analyze.*;
import c2.search.netlas.analyze.StaticData;
import c2.search.netlas.annotation.Static;
import c2.search.netlas.classscanner.ClassScanner;
import c2.search.netlas.scheme.Results;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import netlas.java.scheme.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticSubmit implements Submit {
  private static final Logger LOGGER = LoggerFactory.getLogger(Execute.class);
  private final ClassScanner classScanner;
  private final Data data;

  public StaticSubmit(ClassScanner classScanner, final Data response) {
    this.classScanner = classScanner;
    this.data = response;
  }

  @Override
  public List<Future<Results>> submitTests(ExecutorService executor) {
    return classScanner.getClassesWithAnnotation(Static.class).stream()
        .map(clazz -> executor.submit(() -> runTestsForClass(clazz)))
        .collect(Collectors.toList());
  }

  private Results runTestsForClass(final Class<?> clazz) {
    final StaticData instance = createInstance(clazz);
    StaticAnalyzer analyzer = new StaticAnalyzerImpl(data);
    return analyzer.analyze(instance);
  }

  private StaticData createInstance(final Class<?> clazz) {
    try {
      return (StaticData) clazz.getDeclaredConstructor().newInstance();
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      MethodInvoker.handleInvocationError(e);
      return null;
    }
  }
}
