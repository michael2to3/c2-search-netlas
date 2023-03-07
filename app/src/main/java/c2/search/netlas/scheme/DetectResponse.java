package c2.search.netlas.scheme;

public class DetectResponse {
  private String name;
  private String minVersion;
  private String maxVersion;
  private int countAllTest;
  private int countSuccessTest;

  @Override
  public String toString() {
    return String.format(
        "DetectResponse [name=%s, minVersion=%s, maxVersion=%s, countAllTest=%d,"
            + " countSuccessTest=%d]",
        name, minVersion, maxVersion, countAllTest, countSuccessTest);
  }

  public DetectResponse() {}

  public DetectResponse(
      String name, String minVersion, String maxVersion, int countAllTest, int countSuccessTest) {
    this.name = name;
    this.minVersion = minVersion;
    this.maxVersion = maxVersion;
    this.countAllTest = countAllTest;
    this.countSuccessTest = countSuccessTest;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMinVersion() {
    return minVersion;
  }

  public void setMinVersion(String minVersion) {
    this.minVersion = minVersion;
  }

  public String getMaxVersion() {
    return maxVersion;
  }

  public void setMaxVersion(String maxVersion) {
    this.maxVersion = maxVersion;
  }

  public int getCountAllTest() {
    return countAllTest;
  }

  public void setCountAllTest(int countAllTest) {
    this.countAllTest = countAllTest;
  }

  public int getCountSuccessTest() {
    return countSuccessTest;
  }

  public void setCountSuccessTest(int countSuccessTest) {
    this.countSuccessTest = countSuccessTest;
  }
}
