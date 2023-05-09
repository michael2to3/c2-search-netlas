package c2.search.netlas.execute;

import c2.search.netlas.NetlasCache;
import c2.search.netlas.analyze.StaticAnalyzer;
import c2.search.netlas.analyze.StaticAnalyzerImpl;
import c2.search.netlas.analyze.StaticData;
import c2.search.netlas.annotation.Static;
import c2.search.netlas.classscanner.ClassScanner;
import c2.search.netlas.scheme.Results;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import netlas.java.exception.NetlasRequestException;
import netlas.java.scheme.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticSubmit implements Submit {
  private static final Logger LOGGER = LoggerFactory.getLogger(StaticSubmit.class);
  private final ClassScanner classScanner;

  public StaticSubmit(final ClassScanner classScanner) {
    this.classScanner = classScanner;
  }

  @Override
  public List<Future<Results>> submitTests(final ExecutorService executor) {
    return classScanner.getClassesWithAnnotation(Static.class).stream()
        .map(clazz -> executor.submit(() -> runTestsForClass(clazz)))
        .collect(Collectors.toList());
  }

  private Results runTestsForClass(final Class<?> clazz) {
    final StaticData instance = createInstance(clazz);
    final StaticAnalyzer analyzer = new StaticAnalyzerImpl(getData());
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

  private Data getData() {
    try {
      final var items = NetlasCache.getInstance().response("", 0, null, null, false).getItems();
      if (items.isEmpty()) {
        LOGGER.warn("No static data found");
        return null;
      }
      return items.get(0).getData();
    } catch (final NetlasRequestException e) {
      MethodInvoker.handleInvocationError(e);
      return null;
    }
  }
}
