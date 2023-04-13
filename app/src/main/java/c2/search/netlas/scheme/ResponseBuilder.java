package c2.search.netlas.scheme;

public final class ResponseBuilder {
  // FIXME not correct builder, each instance of Response
  private boolean success = false;
  private Version version = new Version("", "");
  private String description = "";
  private String error = "";

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
