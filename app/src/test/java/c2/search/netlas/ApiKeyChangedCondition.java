package c2.search.netlas;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ApiKeyChangedCondition implements ExecutionCondition {

  private static final String API = System.getenv("API_KEY");

  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
    if (API == null || API.isEmpty()) {
      return ConditionEvaluationResult.disabled("Skipping test: API key has not changed");
    } else {
      return ConditionEvaluationResult.enabled("API key has changed, executing test");
    }
  }
}
