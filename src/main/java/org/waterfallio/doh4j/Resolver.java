package org.waterfallio.doh4j;

import java.util.Objects;

/**
 * The Resolver class represents a DNS resolver for performing DNS over HTTPS (DoH) lookups. Includes URL of the
 * resolvers and the HTTP method to use in a request.
 */
public class Resolver {
  public enum Method {
    GET, POST, PUT
  }

  public final static Resolver GOOGLE = new Resolver("https://dns.google/resolve", Method.POST);
  public final static Resolver CLOUDFLARE = new Resolver("https://cloudflare-dns.com/dns-query", Method.POST);
  public final static Resolver QUAD9 = new Resolver("https://dns.quad9.net:5053/dns-query", Method.POST);

  private String url;
  private Method method = Method.POST;

  Resolver() {
  }

  public Resolver(String url) {
    this.url = Objects.requireNonNull(url, "Url cannot be null");
  }

  public Resolver(String url, Method method) {
    this.url = Objects.requireNonNull(url, "Url cannot be null");
    this.method = Objects.nonNull(method) ? method : this.method;
  }

  public String getUrl() {
    return url;
  }

  void setUrl(String url) {
    this.url = url;
  }

  public Method getMethod() {
    return method;
  }

  void setMethod(Method method) {
    this.method = method;
  }
}
