package org.waterfallio.doh4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.waterfallio.doh4j.exception.Do4jLookupException;
import org.waterfallio.doh4j.specification.Answer;
import org.waterfallio.doh4j.specification.Result;
import org.waterfallio.doh4j.specification.Type;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.waterfallio.doh4j.Doh4j.URL_FORMAT;
import static org.waterfallio.doh4j.Resolver.GOOGLE;

public class Doh4jTest {
  private final ObjectMapper mapper = new ObjectMapper();

  private final String name = "example.com.";
  private final String data = "1.1.1.1";
  private final int type = Type.A;

  @Test
  public void testLookupSuccessful_DefaultResolvers() throws Exception {
    Result result = getResult();

    HttpClient client = mock(HttpClient.class);
    HttpResponse<byte[]> response = (HttpResponse<byte[]>) mock(HttpResponse.class);

    ArgumentCaptor<HttpRequest> requestCaptor =
        ArgumentCaptor.forClass(HttpRequest.class);

    when(response.body())
        .thenReturn(mapper.writeValueAsBytes(result));

    when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(response);

    Result lookup = Doh4j.builder()
        .client(client)
        .build()
        .lookup(name, type);

    verify(client, times(1))
        .send(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));

    HttpRequest request = requestCaptor.getValue();

    assertEquals(result, lookup);
    assertEquals(GOOGLE.getMethod().name(), request.method());
    assertEquals(URI.create(String.format(URL_FORMAT, GOOGLE.getUrl(), name, type)), request.uri());
    assertTrue(request.headers().firstValue("Content-Type").isPresent());
    assertEquals("application/dns-json", request.headers().firstValue("Content-Type").get());
  }

  @Test
  public void testLookupSuccessful_CustomResolvers() throws Exception {
    Result result = getResult();

    HttpClient client = mock(HttpClient.class);
    HttpResponse<byte[]> response = (HttpResponse<byte[]>) mock(HttpResponse.class);

    when(response.body()).thenReturn(mapper.writeValueAsBytes(result));

    when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenThrow(ConnectException.class)
        .thenReturn(response);

    Result lookup = Doh4j.builder()
        .client(client)
        .resolver("https://resolver1.com/resolve")
        .resolver("https://resolver2.com/resolve")
        .build()
        .lookup(name, type);

    verify(client, times(2))
        .send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));

    assertEquals(result, lookup);
  }

  @Test
  public void testLookupFail() throws Exception {
    HttpClient client = mock(HttpClient.class);

    when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenThrow(ConnectException.class);

    Assertions.assertThrows(Do4jLookupException.class, () -> {
      Doh4j.builder()
          .client(client)
          .resolver("https://resolver1.com/resolve")
          .resolver("https://resolver2.com/resolve")
          .build()
          .lookup(name, type);
    });

    verify(client, times(2))
        .send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
  }

  @Test
  public void testLookupAsyncSuccessful_DefaultResolvers() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    Result result = getResult();

    HttpClient client = mock(HttpClient.class);
    HttpResponse<byte[]> response = (HttpResponse<byte[]>) mock(HttpResponse.class);

    ArgumentCaptor<HttpRequest> requestCaptor =
        ArgumentCaptor.forClass(HttpRequest.class);

    when(response.body())
        .thenReturn(mapper.writeValueAsBytes(result));

    when(client.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(CompletableFuture.supplyAsync(() -> response));

    Result lookup = Doh4j.builder()
        .client(client)
        .build()
        .lookupAsync(name, type)
        .whenComplete((r, exception) -> latch.countDown())
        .get();

    verify(client, times(1))
        .sendAsync(requestCaptor.capture(), any(HttpResponse.BodyHandler.class));

    HttpRequest request = requestCaptor.getValue();

    assertEquals(result, lookup);
    assertEquals(GOOGLE.getMethod().name(), request.method());
    assertEquals(URI.create(String.format(URL_FORMAT, GOOGLE.getUrl(), name, type)), request.uri());
    assertTrue(request.headers().firstValue("Content-Type").isPresent());
    assertEquals("application/dns-json", request.headers().firstValue("Content-Type").get());
    assertTrue(latch.await(5, TimeUnit.SECONDS));
  }

  @Test
  public void testLookupAsyncSuccessful_CustomResolvers() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    Result result = getResult();

    HttpClient client = mock(HttpClient.class);
    HttpResponse<byte[]> response = (HttpResponse<byte[]>) mock(HttpResponse.class);

    when(response.body()).thenReturn(mapper.writeValueAsBytes(result));

    when(client.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(CompletableFuture.failedFuture(new ConnectException()))
        .thenReturn(CompletableFuture.supplyAsync(() -> response));

    Result lookup = Doh4j.builder()
        .client(client)
        .resolver("https://resolver1.com/resolve")
        .resolver("https://resolver2.com/resolve")
        .build()
        .lookupAsync(name, type)
        .get();

    latch.countDown();

    verify(client, times(2))
        .sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));

    assertEquals(result, lookup);
    assertTrue(latch.await(5, TimeUnit.SECONDS));
  }

  @Test
  public void testLookupAsyncFail() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    HttpClient client = mock(HttpClient.class);

    when(client.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(CompletableFuture.failedFuture(new ConnectException()));

    ExecutionException exception = Assertions.assertThrows(ExecutionException.class, () -> {
      Doh4j.builder()
          .client(client)
          .resolver("https://resolver1.com/resolve")
          .resolver("https://resolver2.com/resolve")
          .build()
          .lookupAsync(name, type)
          .whenComplete((r, e) -> latch.countDown())
          .get();
    });

    verify(client, times(2))
        .sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));

    assertInstanceOf(Do4jLookupException.class, exception.getCause());
    assertTrue(latch.await(5, TimeUnit.SECONDS));
  }

  private Result getResult() {
    Answer answer = new Answer();

    answer.setName(name);
    answer.setType(type);
    answer.setData(data);

    Result result = new Result();

    result.setStatus(0);
    result.setAnswer(List.of(answer));

    return result;
  }
}
