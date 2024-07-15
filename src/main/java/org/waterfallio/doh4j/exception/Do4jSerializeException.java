package org.waterfallio.doh4j.exception;

public class Do4jSerializeException extends RuntimeException {
  public Do4jSerializeException() {
    super();
  }

  public Do4jSerializeException(String message) {
    super(message);
  }

  public Do4jSerializeException(String message, Throwable cause) {
    super(message, cause);
  }

  public Do4jSerializeException(Throwable cause) {
    super(cause);
  }
}
