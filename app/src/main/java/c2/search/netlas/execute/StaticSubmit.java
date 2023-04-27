package c2.search.netlas.execute;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import c2.search.netlas.analyze.StaticAnalyzer;
import c2.search.netlas.analyze.StaticData;
import c2.search.netlas.annotation.Static;
import c2.search.netlas.classscanner.ClassScanner;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.Results;
import netlas.java.scheme.Data;

public class StaticSubmit implements Submit {
  private static final Logger LOGGER = LoggerFactory.getLogger(Execute.class);
  private static final int TIMEOUT_SINGLE = 5;
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
    final List<CompletableFuture<Response>> futures = invokeTestMethods(instance);
    return createResultsFromFutures(clazz, futures);
  }

  private List<CompletableFuture<Response>> invokeTestMethods(final StaticData instance) {
    final List<CompletableFuture<Response>> futures = new ArrayList<>();
    StaticAnalyzer analyzer = new StaticAnalyzerImpl(data);
    return futures;
  }

  private Results createResultsFromFutures(
      final Class<?> clazz, final List<CompletableFuture<Response>> futures) {
    final List<Response> responses = new ArrayList<>();
    for (final CompletableFuture<Response> future : futures) {
      try {
        final Response response = future.get(TIMEOUT_SINGLE, TimeUnit.SECONDS);
        if (response != null) {
          responses.add(response);
        }
      } catch (InterruptedException | ExecutionException | TimeoutException e) {
        MethodInvoker.handleInvocationError(e);
      }
    }
    final Results results = new Results();
    results.addResponse(getNameOfClass(clazz), responses);
    return results;
  }

  private String getNameOfClass(final Class<?> clazz) {
    final Static detect = clazz.getAnnotation(Static.class);
    String nameOfStatic = detect.name();
    if (nameOfStatic == null || nameOfStatic.isEmpty()) {
      nameOfStatic = clazz.getName();
    }
    return nameOfStatic;
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
