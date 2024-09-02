package org.esfe.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.esfe.models.Order;
import org.esfe.models.Payment;
import org.esfe.models.User;
import org.esfe.models.dtos.order.ReturnCreateOrder;
import org.esfe.models.dtos.wompi.PaymentLinkResponse;
import org.esfe.models.dtos.wompi.WompiWebhook;
import org.esfe.models.enums.OrderStatus;
import org.esfe.services.implementations.WompiService;
import org.esfe.services.interfaces.IOrderService;
import org.esfe.services.interfaces.IPaymentService;
import org.esfe.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private WompiService wompiService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IPaymentService paymentService;

    private static final String FILE_PATH = "wompi-webhooks.json";
    private List<WompiWebhook> webhookList = new ArrayList<>();

    @PostMapping(path = "/orders/new", produces = "application/json")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> model) {

        Map<String, Object> userMap = (Map<String, Object>) model.get("user");
        List<Map<String, Object>> orderItemsMap = (List<Map<String, Object>>) model.get("orderItems");

        String userIdStr = (String) userMap.get("id");
        Integer userId = Integer.parseInt(userIdStr);

        Optional<User> userOpt = userService.buscarPorId(userId);
        if (!userOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userOpt.get();
        ReturnCreateOrder result = orderService.createOrderMap(user, orderItemsMap);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order created successfully");
        response.put("redirectUrl", "/orders/detail/" + result.getOrder().getId());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Nuevo endpoint para recibir el webhook de Wompi
    @PostMapping(path = "/wompi/webhook", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> receiveWompiWebhook(@RequestBody WompiWebhook webhookData) {

        Integer identificador = Integer.parseInt(webhookData.getEnlacePago().getIdentificadorEnlaceComercio());

        Optional<Order> orderOpt = orderService.buscarPorId(identificador);

        if (!orderOpt.isPresent()) {
            return new ResponseEntity<>("No se encontró la orden asociada al identificador", HttpStatus.NOT_FOUND);
        }

        Order order = orderOpt.get();

        Optional<Payment> paymentOpt = paymentService.findByOrder(order);

        if (!paymentOpt.isPresent()) {
            return new ResponseEntity<>("No se encontró el pago asociado al identificador", HttpStatus.NOT_FOUND);
        }

        Payment payment = paymentOpt.get();

        // if (!payment.getStatus().equals("PAID")) {
        //     return new ResponseEntity<>("El pago ya ha sido procesado", HttpStatus.BAD_REQUEST);
        // }

        if(webhookData.getResultadoTransaccion().equals("ExitosaAprobada")) {
            payment.setStatus("PAID");
        } else {
            payment.setStatus("REJECTED");
        }
        
        payment.setIdCuenta(webhookData.getIdCuenta());
        payment.setFechaTransaccion(webhookData.getFechaTransaccion());
        payment.setModuloUtilizado(webhookData.getModuloUtilizado());
        payment.setFormaPagoUtilizada(webhookData.getFormaPagoUtilizada());
        payment.setIdTransaccion(webhookData.getIdTransaccion());
        payment.setResultadoTransaccion(webhookData.getResultadoTransaccion());
        payment.setCodigoAutorizacion(webhookData.getCodigoAutorizacion());
        payment.setIdIntentoPago(webhookData.getIdIntentoPago());

        order.setStatus(OrderStatus.DELIVERED);

        orderService.createOEditar(order);
        paymentService.createOEditar(payment);

        // Agregar el webhook a la lista en memoria
        webhookList.add(webhookData);

        // Guardar el webhook en un archivo local
        saveWebhookToFile();

        return new ResponseEntity<>("Webhook recibido exitosamente", HttpStatus.OK);
    }

    // Endpoint para consultar los webhooks recibidos
    @GetMapping(path = "/wompi/webhook", produces = "application/json")
    public ResponseEntity<List<WompiWebhook>> getWebhooks() {
        return new ResponseEntity<>(webhookList, HttpStatus.OK);
    }

    // Método para guardar el webhook en un archivo local con manejo de concurrencia
    private void saveWebhookToFile() {
        ObjectMapper mapper = new ObjectMapper();
        synchronized (this) { // Manejo de concurrencia para escritura en archivo
            try {
                File file = new File(FILE_PATH);
                FileWriter fileWriter = new FileWriter(file, true);

                for (WompiWebhook webhook : webhookList) {
                    String jsonString = mapper.writeValueAsString(webhook);
                    fileWriter.write(jsonString + System.lineSeparator());
                }
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping(path = "/wompi/generate-link", produces = "application/json")
    public ResponseEntity<PaymentLinkResponse> generatePaymentLink(@RequestBody Map<String, Object> requestData) {
        try {
            Double monto = Double.valueOf(requestData.get("monto").toString());
            Integer idOrder = Integer.valueOf(requestData.get("idOrder").toString());

            PaymentLinkResponse response = wompiService.generateLink(monto, idOrder);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
