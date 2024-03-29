package c2.search.netlas.scheme;

public class Version implements Comparable<Version> {
  private String max;
  private String min;

  public Version(final String max, final String min) {
    this.max = max;
    this.min = min;
  }

  @Override
  public String toString() {
    return String.format("%s-%s", max, min);
  }

  @Override
  public int compareTo(final Version other) {
    int result = this.min.compareTo(other.min);
    if (result == 0) {
      result = this.max.compareTo(other.max);
    }
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    boolean result;
    if (this == obj) {
      result = true;
    } else if (obj == null || getClass() != obj.getClass()) {
      result = false;
    } else {
      final Version version = (Version) obj;
      result = min.equals(version.min) && max.equals(version.max);
    }
    return result;
  }

  @Override
  public int hashCode() {
    final int len = 31;
    return min.hashCode() * len + max.hashCode();
  }

  public boolean isEmpty() {
    return min == null || max == null || min.isEmpty() || max.isEmpty();
  }

  public String getMin() {
    return min;
  }

  public void setMin(final String min) {
    this.min = min;
  }

  public String getMax() {
    return max;
  }

  public void setMax(final String max) {
    this.max = max;
  }
}
