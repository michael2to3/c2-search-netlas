package c2.search.netlas.scheme;

public class Response {
  private boolean success;
  private Version version;
  private String description;
  private String error;

  private Response() {
    this.success = false;
    this.version = new Version("", "");
    this.description = "";
    this.error = "";
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

  public static Builder newBuilder() {
    return new Response().new Builder();
  }

  public final class Builder {

    private Builder() {}

    public Builder setVersion(final Version version) {
      Response.this.version = version;
      return this;
    }

    public Builder setSuccess(final boolean success) {
      Response.this.success = success;
      return this;
    }

    public Builder setDescription(final String description) {
      Response.this.description = description;
      return this;
    }

    public Builder setError(final String error) {
      Response.this.error = error;
      return this;
    }

    public Response build() {
      return new Response(success, version, description, error);
    }
  }
}
