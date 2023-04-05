package c2.search.netlas.scheme;


public class Response {
  private boolean success;
  private Version version;
  private String description;
  private String error;

  public Response() {}

  public Response(final boolean success) {
    this(success, new Version());
  }

  public Response(final boolean success, final Version version) {
    this(success, version, "");
  }

  public Response(final boolean success, final Version version, final String description) {
    this(success, version, description, "");
  }

  public Response(
      final boolean success, final Version version, final String description, final String error) {
    this.success = success;
    this.version = version;
    this.description = description;
    this.error = error;
  }

  public Version getVersion() {
    return version;
  }

  public void setVersion(final Version version) {
    this.version = version;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(final boolean success) {
    this.success = success;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getError() {
    return error;
  }

  public void setError(final String error) {
    this.error = error;
  }
}
