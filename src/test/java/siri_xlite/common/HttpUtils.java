package siri_xlite.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HttpUtils {

    public static HttpResponse<String> get(String url) throws Exception {
        TimeUnit.SECONDS.sleep(1);
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(1)).build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void printHeader(HttpResponse<String> response) throws Exception {
        HttpHeaders headers = response.headers();
        headers.map().forEach((key, values) -> {
            System.out.println(String.format("%s: %s", key, values));
        });
    }

    public static void prettyPrint(HttpResponse<String> response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object value = mapper.readValue(response.body(), Object.class);
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        System.out.println(result);
    }
}
