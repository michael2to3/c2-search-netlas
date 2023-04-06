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

  public ResponseBuilder success(boolean success) {
    this.success = success;
    return this;
  }

  public ResponseBuilder version(Version version) {
    this.version = version;
    return this;
  }

  public ResponseBuilder description(String description) {
    this.description = description;
    return this;
  }

  public ResponseBuilder error(String error) {
    this.error = error;
    return this;
  }

  public Response build() {
    return new Response(success, version, description, error);
  }
}
