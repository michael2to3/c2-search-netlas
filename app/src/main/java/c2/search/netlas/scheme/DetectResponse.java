package c2.search.netlas.scheme;

public class DetectResponse {
  private String name;
  private Version version;
  private int countAllTest;
  private int countSuccessTest;

  @Override
  public String toString() {
    return String.format(
        "DetectResponse [name=%s, version=%s, countAllTest=%d, countSuccessTest=%d]",
        name, version, countAllTest, countSuccessTest);
  }

  public DetectResponse() {
    this("", "", "", 0, 0);
  }

  public DetectResponse(
      String name, String minVersion, String maxVersion, int countAllTest, int countSuccessTest) {
    this.name = name;
    this.version = new Version(minVersion, maxVersion);
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
    return version.getMin();
  }

  public void setMinVersion(String minVersion) {
    this.version.setMin(minVersion);
  }

  public String getMaxVersion() {
    return version.getMax();
  }

  public void setMaxVersion(String maxVersion) {
    this.version.setMax(maxVersion);
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
