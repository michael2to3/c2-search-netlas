package c2.search.netlas.execute;

import c2.search.netlas.scheme.Results;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public interface Submit {
  List<Future<Results>> submitTests(ExecutorService executor);
}
