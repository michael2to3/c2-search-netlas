package c2.search.netlas.query;

public abstract class QueryBuilder {
  private String separator;

  protected QueryBuilder() {
    this.separator = " AND ";
  }

  public void setSeparator(final String separator) {
    this.separator = separator;
  }

  protected String getSeparator() {
    return separator;
  }

  public abstract String build();
}
