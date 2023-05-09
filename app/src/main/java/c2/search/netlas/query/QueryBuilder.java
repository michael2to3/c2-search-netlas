package c2.search.netlas.query;

public interface QueryBuilder {
  public abstract String build();

  public abstract void setSeparator(final String separator);

  public abstract String getSeparator();
}
