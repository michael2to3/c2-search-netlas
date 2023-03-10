package c2.search.netlas.scheme;

import java.util.Objects;

public class Response {
  private boolean success;
  private Version version;
  private String description;
  private String error;

  public Response() {}

  public Response(boolean success) {
    this(success, new Version());
  }

  public Response(boolean success, Version version) {
    this(success, version, "");
  }

  public Response(boolean success, Version version, String description) {
    this(success, version, description, "");
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Response response = (Response) o;
    return success == response.success
        && version.equals(response.version)
        && description.equals(response.description)
        && error.equals(response.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, version, description, error);
  }
}
