package siri_xlite.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class HttpUtils {

    private static SSLContext sslContext;
    private static SSLParameters sslParameters;

    static {
        try {
            sslContext = getSSLContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sslParameters = getSSLParameters();
    }

    private static SSLContext getSSLContext() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        return sslContext;
    }

    private static SSLParameters getSSLParameters() {
        SSLParameters sslParameters = new SSLParameters();
        sslParameters.setEndpointIdentificationAlgorithm("");
        return sslParameters;
    }

    public static HttpResponse<String> get(String url) throws Exception {
        TimeUnit.SECONDS.sleep(1);
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(1))
                .sslContext(sslContext)
                .sslParameters(sslParameters)
                .build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void printHeader(HttpResponse<String> response) {
        HttpHeaders headers = response.headers();
        headers.map().forEach((key, values) -> System.out.println(String.format("%s: %s", key, values)));
    }

    public static void prettyPrint(HttpResponse<String> response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object value = mapper.readValue(response.body(), Object.class);
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        System.out.println(result);
    }
}
