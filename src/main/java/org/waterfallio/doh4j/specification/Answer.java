package org.waterfallio.doh4j.specification;

import java.util.Objects;

/**
 * <a href="https://developers.google.com/speed/public-dns/docs/doh">DNS-over-HTTPS (DoH)</a>
 * <a href="https://developers.google.com/speed/public-dns/docs/doh/json">JSON API for DNS over HTTPS (DoH)</a>
 */
public class Answer {
  private String name;
  private int type;
  private int TTL;
  private String data;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getTTL() {
    return TTL;
  }

  public void setTTL(int TTL) {
    this.TTL = TTL;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "Answer{" +
        "name='" + name + '\'' +
        ", type=" + type +
        ", TTL=" + TTL +
        ", data='" + data + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Answer answer = (Answer) o;

    return type == answer.type &&
        TTL == answer.TTL &&
        Objects.equals(name, answer.name) &&
        Objects.equals(data, answer.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type, TTL, data);
  }
}
