# Doh4j: DNS over HTTPS (DoH) for Java

The Doh4j library is a powerful yet simple DNS Over HTTPS (DoH) client implementation for Java 11+, packaged neatly into a streamlined and powerful API. The Doh4j client uses Google, Cloudflare, and Quad9 DoH resolvers by default, but it also supports custom RFC 8484-compliant resolvers.  

## Features

- Perform DNS lookup over HTTP: The Doh4jClient class provides a client for performing DNS over HTTPS (DoH) lookups using a list of resolvers.
- Configurable Resolvers: By default, Google, Cloudflare, and Quad9 are used as resolvers. However, custom resolvers can be supplied.
- Fallback Mechanism: If a DNS lookup fails with the first resolver, the client tries the next one until a resolver responds or no resolvers are left.

## Installation
The Doh4j library can be easily installed using JitPack, see Gradle and Maven examples below.

### Gradle
Add the following to your build.gradle file:

```
repositories {
    mavenCentral()
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation 'com.github.thewaterfall:doh4j:1.0.0'
}
```

### Maven
Add the following to your pom.xml file:

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.thewaterfall</groupId>
        <artifactId>doh4j</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## Usage

The Doh4j client provides a simple and straightforward interface, making the process of DNS lookups easy. Below are examples of how you can carry out DNS lookups using predefined and custom resolvers.

### Lookup with predefined resolvers

```
Doh4j.newClient()
    .lookup("example.com", Type.A);
```

### Lookup with custom resolvers

```
Doh4j.build()
    .resolver("https://resolve.com/resolve")
    .build()
    .lookup("example.com", Type.A);
```


### Lookup with custom HTTP client

You can also use a custom java.net HTTP client while performing DNS lookups. This feature can be useful in situations where there is a need for custom configuration for HTTP requests like timeouts, handlers, proxies, and more. Here's how you can perform a DNS lookup with a custom resolver and a custom HTTP client:

```
HttpClient client = HttpClient.newHttpClient();

Doh4j.build()
    .client(client)
    .resolver("https://resolve.com/resolve")
    .build()
    .lookup("example.com", Type.A);
```
