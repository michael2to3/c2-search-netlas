package c2.search.netlas.scheme;

public class Version implements Comparable<Version> {
  private String min;
  private String max;

  public Version(String min, String max) {
    this.min = min;
    this.max = max;
  }

  public Version() {}

  @Override
  public String toString() {
    return String.format("%s-%s", min, max);
  }

  @Override
  public int compareTo(Version other) {
    int result = this.min.compareTo(other.min);
    if (result == 0) {
      result = this.max.compareTo(other.max);
    }
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Version version = (Version) o;
    return min.equals(version.min) && max.equals(version.max);
  }

  @Override
  public int hashCode() {
    return min.hashCode() * 31 + max.hashCode();
  }

  public String getMin() {
    return min;
  }

  public void setMin(String min) {
    this.min = min;
  }

  public String getMax() {
    return max;
  }

  public void setMax(String max) {
    this.max = max;
  }
}
