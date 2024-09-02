package org.esfe.services.implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.esfe.models.dtos.wompi.PaymentLinkRequest;
import org.esfe.models.dtos.wompi.PaymentLinkRequest.Configuracion;
import org.esfe.models.dtos.wompi.PaymentLinkRequest.FormaPago;
import org.esfe.models.dtos.wompi.PaymentLinkResponse;
import org.esfe.services.interfaces.IWompiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

@Service
public class WompiService implements IWompiService {

    @Value("${wompi.client.id}")
    private String clientId;

    @Value("${wompi.client.secret}")
    private String clientSecret;

    @Value("${wompi.emails.notificacion}")
    private String emailsNotificacion;

//    @Value("${app.host}")
    private String host = "https://springboot-ecommerce-test.up.railway.app";

    @Value("${wompi.api.url}")
    private String apiUrl;

    public PaymentLinkResponse generateLink(String identificadorEnlaceComercio, Double monto, Integer idOrder) {
        try {
            String authToken = getAuth();

            //
            PaymentLinkRequest request = new PaymentLinkRequest();
            request.setIdAplicativo(clientId);
            request.setIdentificadorEnlaceComercio(identificadorEnlaceComercio);
            request.setMonto(monto);
            request.setNombreProducto("Tu carrito de compras");

            //
            FormaPago formaPago = new FormaPago();
            formaPago.setPermitirTarjetaCreditoDebido(true);
            request.setFormaPago(formaPago);

            //
            Configuracion configuracion = new Configuracion();
            configuracion.setEsMontoEditable(false);
            configuracion.setEsCantidadEditable(false);
            configuracion.setCantidadPorDefecto(1);
            configuracion.setDuracionInterfazIntentoMinutos(60);
            configuracion.setUrlRetorno(host + "/payment/result/" + idOrder);
            configuracion.setEmailsNotificacion(emailsNotificacion);
            configuracion.setUrlWebhook(host + "/api/wompi/webhook");
            configuracion.setNotificarTransaccionCliente(true);
            request.setConfiguracion(configuracion);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(request);

            // Crear y enviar la solicitud POST
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/EnlacePago"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Deserializar la respuesta en un PaymentLinkResponse
                return objectMapper.readValue(response.body(), PaymentLinkResponse.class);
            } else {
                throw new RuntimeException("Failed to generate link with Wompi: " + response.body());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error during link generation", e);
        }
    }

    private String getAuth() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            Map<String, String> parameters = Map.of(
                "grant_type", "client_credentials",
                "audience", "wompi_api",
                "client_id", clientId,
                "client_secret", clientSecret
            );

            StringJoiner formParams = new StringJoiner("&");
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                formParams.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + 
                               URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            }

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://id.wompi.sv/connect/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formParams.toString()))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
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
