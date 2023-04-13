package c2.search.netlas.execute;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import c2.search.netlas.annotation.BeforeAll;
import c2.search.netlas.annotation.Detect;
import c2.search.netlas.annotation.Test;
import c2.search.netlas.classscanner.ClassScanner;
import c2.search.netlas.classscanner.FieldValues;
import c2.search.netlas.scheme.Response;
import c2.search.netlas.scheme.ResponseBuilder;
import c2.search.netlas.scheme.Results;

public class ExecuteTest {

  private Execute checker;
  private ClassScanner classScanner;

  @BeforeEach
  public void setup() {
    classScanner = mock(ClassScanner.class);
    FieldValues fields = mock(FieldValues.class);
    checker = new Execute(fields);
  }

  @Test
  public void testRun() throws Exception {
    Class<?> targetClass = DummyTarget.class;
    when(classScanner.getClassesWithAnnotation(Detect.class)).thenReturn(List.of(targetClass));
    when(classScanner.getClassesWithAnnotation(Detect.class)).thenReturn(List.of(targetClass));

    Results results = checker.run();

    assertEquals(1, results.getResponses().size());
  }

  @Detect(name = "DummyTarget")
  class DummyTarget {
    @BeforeAll
    public void beforeAllMethod() {}

    @Test(description = "testMethod description")
    public Response testMethod() {
      return new ResponseBuilder().setSuccess(true).build();
    }

    public boolean testMethod2() {
      return true;
    }
  }
}
