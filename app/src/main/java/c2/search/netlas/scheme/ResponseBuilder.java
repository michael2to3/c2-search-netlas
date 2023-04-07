package c2.search.netlas.scheme;

public class ResponseBuilder {
  private boolean success;
  private Version version;
  private String description;
  private String error;

  public ResponseBuilder() {
    success = false;
    version = new Version("", "");
    description = "";
    error = "";
  }

  public ResponseBuilder setSuccess(final boolean success) {
    this.success = success;
    return this;
  }

  public ResponseBuilder setVersion(final Version version) {
    this.version = version;
    return this;
  }

  public ResponseBuilder setDescription(final String description) {
    this.description = description;
    return this;
  }

  public ResponseBuilder setError(final String error) {
    this.error = error;
    return this;
  }

  public Response build() {
    return new Response(success, version, description, error);
  }
}
