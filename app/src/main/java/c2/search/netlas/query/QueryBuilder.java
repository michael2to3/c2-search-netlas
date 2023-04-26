package c2.search.netlas.query;

public interface QueryBuilder {
  public String build();

  public void setSeparator(final String separator);

  public String getSeparator();
}
