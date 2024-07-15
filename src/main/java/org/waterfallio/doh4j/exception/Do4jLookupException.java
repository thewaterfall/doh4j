package org.waterfallio.doh4j.exception;

public class Do4jLookupException extends RuntimeException {
  public Do4jLookupException() {
    super();
  }

  public Do4jLookupException(String message) {
    super(message);
  }

  public Do4jLookupException(String message, Throwable cause) {
    super(message, cause);
  }

  public Do4jLookupException(Throwable cause) {
    super(cause);
  }
}
