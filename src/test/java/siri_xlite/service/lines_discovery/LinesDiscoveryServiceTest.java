package siri_xlite.service.lines_discovery;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.HttpURLConnection;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static siri_xlite.common.HttpUtils.*;

@SpringBootTest
@Slf4j
class LinesDiscoveryServiceTest {

    // [DSU] recuperer le certificate du serveur siri-xlite.pem et l'enregistrer dans le keystore de la jvm
    // sudo keytool -import -alias siri-xlite -file ~/Téléchargements/siri-xlite.pem -keystore /etc/pki/java/cacerts

    @Test
    void linesDiscovery() throws Exception {
        HttpResponse<String> response = get("https://localhost:8443/siri-xlite/lines-discovery");
        printHeader(response);
        prettyPrint(response);

        assertEquals(response.statusCode(), HttpURLConnection.HTTP_OK);
        // TODO test response values
    }
}