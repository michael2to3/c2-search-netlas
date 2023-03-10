package c2.search.netlas.scheme;

public class Response {
  private boolean success;
  private Version version;
  private String description;
  private String error;

  public Response(boolean success) {
    this.success = success;
  }

  public Response(boolean success, Version version) {
    this.version = version;
    this.success = success;
  }

  public Response(boolean success, Version version, String description) {
    this.success = success;
    this.version = version;
    this.description = description;
  }

  public Response(boolean success, Version version, String description, String error) {
    this.success = success;
    this.version = version;
    this.description = description;
    this.error = error;
  }

  public Version getVersion() {
    return version;
  }

  public void setVersion(Version version) {
    this.version = version;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  @Override
  public String toString() {
    return String.format(
        "Response(version=%s, success=%s, description=%s, error=%s)",
        version, success, description, error);
  }
}
