package org.waterfallio.doh4j;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.waterfallio.doh4j.exception.Do4jLookupException;
import org.waterfallio.doh4j.exception.Do4jSerializeException;
import org.waterfallio.doh4j.specification.Result;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.waterfallio.doh4j.Resolver.*;

/**
 * <p>The Doh4j class provides a client implementation to perform DNS over HTTPS (DoH) lookups.</p>
 *
 * <p>Instantiate a {@link Doh4jClient} with {@link Doh4j#newClient()} to use with default resolvers (Google,
 * Cloudflare, Quad9, or with {@link Doh4j#builder()} to provide your own resolvers.</p>
 *
 * @see Doh4jClient#lookup(String, int)
 */
public class Doh4j {
  private final static Logger log = LoggerFactory.getLogger(Doh4j.class);

  private static HttpClient client =
      HttpClient.newHttpClient();

  private final static ObjectMapper mapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  public final static String URL_FORMAT = "%s?name=%s&type=%s";

  /**
   * Creates a new instance of Doh4jClient with default resolvers (Google, Cloudflare, Quad9).
   *
   * <p>Example of lookup with default resolvers:</p>
   * <pre>{@code
   * Do4J.newClient()
   *  .lookup("example.com", Type.A)
   * }</pre>
   *
   * @return a new Doh4jClient object.
   * @see Doh4jClient#lookup(String, int)
   */
  public static Doh4jClient newClient() {
    return new Doh4jClient(client);
  }

  /**
   * <p>Returns a new instance of the Doh4jClientBuilder.</p>
   *
   * <p>Example of lookup with custom resolvers:</p>
   * <pre>{@code
   * Do4J.builder()
   *  .resolver("https://resolver1.com/resolve")
   *  .resolver("https://resolver2.com/dns-query")
   *  .build()
   *  .lookup("example.com", Type.A)
   * }</pre>
   *
   * @return a new instance of the Doh4jClientBuilder.
   * @see Doh4jClient#lookup(String, int)
   */
  public static Doh4jClient.Doh4jClientBuilder builder() {
    return new Doh4jClient.Doh4jClientBuilder();
  }

  /**
   * The Doh4jClient class provides a client for performing DNS over HTTPS (DoH) lookups using a list of resolvers.
   *
   * @see Doh4jClient#lookup(String, int)
   */
  public static class Doh4jClient {
    /**
     * Resolvers provide the URL and HTTP method required for performing DNS over HTTPS (DoH) lookups.
     */
    private List<Resolver> resolvers = new ArrayList<>(
        List.of(GOOGLE, CLOUDFLARE, QUAD9)
    );

    /**
     * The HTTP client used for performing DNS over HTTPS (DoH) lookups.
     */
    private final HttpClient client;

    /**
     * Doh4jClient is a class that represents a client for performing DNS over HTTPS (DoH) lookups.
     * It allows the user to specify a list of resolvers to use for the lookups.
     */
    private Doh4jClient(HttpClient client, List<Resolver> resolvers) {
      this.client = client;

      if (!resolvers.isEmpty()) {
        this.resolvers = resolvers;
      }
    }

    /**
     * Doh4jClient is a class that represents a client for performing DNS over HTTPS (DoH) lookups.
     * It allows the user to specify a list of resolvers to use for the lookups.
     */
    private Doh4jClient(HttpClient client, Resolver... resolvers) {
      this.client = client;

      if (resolvers.length != 0) {
        this.resolvers = List.of(resolvers);
      }
    }

    /**
     * <p>Performs a DNS lookup using a list of resolvers (by default Google, Cloudflare, Quad9). If the first resolver
     * fails to respond, i.e. unreachable, continues with the next resolver, until a resolver responds
     * or no resolvers are left.</p>
     *
     * <p>Example of lookup with default resolvers (Google, Cloudflare, Quad9):</p>
     * <pre>{@code
     * Do4J.newClient()
     *  .lookup("example.com", Type.A)
     * }</pre>
     *
     * <p>Example of lookup with custom resolvers:</p>
     * <pre>{@code
     * Do4J.builder()
     *  .resolver("https://resolver1.com/resolve")
     *  .resolver("https://resolver2.com/dns-query") // Will be used if first resolver is not reachable
     *  .build()
     *  .lookup("example.com", Type.A)
     * }</pre>
     *
     * @param name the domain name to lookup
     * @param type the type of DNS record to retrieve, see {@link org.waterfallio.doh4j.specification.Type}
     * @return {@link Result} the result of the lookup
     * @throws Do4jLookupException if unable to connect to any of the resolvers
     */
    public Result lookup(String name, int type) throws Do4jLookupException {
      return resolvers.stream()
          .flatMap(resolver -> {
            try {
              if (log.isDebugEnabled()) {
                log.debug("Perform look up with {} resolver for {} and {} type", resolver.getUrl(), name, type);
              }

              return Stream.of(doLookup(resolver, name, type));
            } catch (Do4jLookupException e) {
              if (log.isDebugEnabled()) {
                log.debug("Failed to lookup", e);
              }

              return Stream.empty();
            }
          })
          .findFirst()
          .orElseThrow(() -> new Do4jLookupException("Failed to lookup with provided resolvers"));
    }

    private Result doLookup(Resolver resolver, String name, int type) throws Do4jLookupException {
      try {
        return deserialize(client.send(getRequest(resolver, name, type), HttpResponse.BodyHandlers.ofByteArray()));
      } catch (IOException | InterruptedException | Do4jSerializeException e) {
        throw new Do4jLookupException(e);
      }
    }

    private HttpRequest getRequest(Resolver resolver, String name, int type) {
      return HttpRequest.newBuilder()
          .uri(URI.create(String.format(URL_FORMAT, resolver.getUrl(), name, type)))
          .method(resolver.getMethod().name(), HttpRequest.BodyPublishers.noBody())
          .header("Content-Type", "application/dns-json")
          .build();
    }

    private Result deserialize(HttpResponse<byte[]> response) throws Do4jLookupException {
      if (response.body().length == 0) {
        throw new Do4jSerializeException("Response body is empty");
      }

      try {
        return mapper.readValue(response.body(), Result.class);
      } catch (IOException e) {
        throw new Do4jSerializeException(e);
      }
    }

    /**
     * Doh4jClientBuilder is a class that provides a builder for creating custom instances of Doh4jClient,
     * providing your own resolvers, or custom {@link HttpClient} with {@link Doh4jClientBuilder#client(HttpClient)}.
     */
    public static class Doh4jClientBuilder {
      private List<Resolver> resolvers = new ArrayList<>();
      private HttpClient client = Doh4j.client;

      /**
       * Sets the HTTP client to use for performing DNS over HTTPS (DoH) lookups.
       *
       * @param client the HTTP client to use
       * @return the Doh4jClientBuilder instance
       */
      public Doh4jClientBuilder client(HttpClient client) {
        this.client = client;
        return this;
      }

      /**
       * Adds a resolver to the Doh4jClientBuilder.
       *
       * @param url    The URL of the resolver, e.g. "https://dns.google/resolve"
       * @param method The HTTP method to use when requesting resolver, e.g. "Method.GET"
       * @return The Doh4jClientBuilder instance.
       * @see Method
       */
      public Doh4jClientBuilder resolver(String url, Method method) {
        resolvers.add(new Resolver(url, method));
        return this;
      }

      /**
       * Adds a resolver to the Doh4jClientBuilder.
       *
       * @param url The URL of the resolver, e.g. "https://dns.google/resolve"
       * @return The Doh4jClientBuilder instance.
       * @see Method
       */
      public Doh4jClientBuilder resolver(String url) {
        resolvers.add(new Resolver(url));
        return this;
      }

      /**
       * Builds and returns a new instance of Doh4jClient.
       *
       * @return the newly built Doh4jClient instance
       * @see Doh4jClient#lookup(String, int)
       */
      public Doh4jClient build() {
        return new Doh4jClient(client, resolvers);
      }
    }
  }
}
