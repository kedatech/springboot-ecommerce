package org.esfe.services.implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.esfe.models.dtos.wompi.PaymentLinkResponse;
import org.esfe.services.interfaces.IWompiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.StringJoiner;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class WompiService implements IWompiService {

    @Value("${wompi.client.id}")
    private String clientId;

    @Value("${wompi.client.secret}")
    private String clientSecret;

    public WompiService() {
    }

    public PaymentLinkResponse generateLink(Integer idEnlace, String urlEnlace) {
        // Implementación pendiente
        return null;
    }

    private String getAuth() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Datos del cuerpo de la solicitud
            Map<String, String> parameters = Map.of(
                "grant_type", "client_credentials",
                "audience", "wompi_api",
                "client_id", clientId,
                "client_secret", clientSecret
            );

            // Codificación del cuerpo en formato x-www-form-urlencoded
            StringJoiner formParams = new StringJoiner("&");
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                formParams.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + 
                               URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            }

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://id.wompi.sv/connect/token")) // URL del endpoint
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formParams.toString()))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Usar ObjectMapper para deserializar el JSON y extraer el access_token
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.body());
                return jsonNode.get("access_token").asText();
            } else {
                throw new RuntimeException("Failed to authenticate with Wompi: " + response.body());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error during authentication", e);
        }
    }
}
