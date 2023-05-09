package c2.search.netlas.query;

public interface QueryBuilder {
  public abstract String build();

  public abstract void setSeparator(String separator);

  public abstract String getSeparator();
}
