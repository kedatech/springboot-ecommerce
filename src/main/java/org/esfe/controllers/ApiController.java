package org.esfe.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.esfe.models.User;
import org.esfe.models.dtos.wompi.WompiWebhook;
import org.esfe.services.interfaces.IOrderItemService;
import org.esfe.services.interfaces.IOrderService;
import org.esfe.services.interfaces.IProductService;
import org.esfe.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IOrderItemService orderItemService;

    @Autowired
    private IUserService userService;

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
        orderService.createOrderMap(user, orderItemsMap);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order created successfully");
        response.put("redirectUrl", "/");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Nuevo endpoint para recibir el webhook de Wompi
    @PostMapping(path = "/wompi/webhook", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> receiveWompiWebhook(@RequestBody WompiWebhook webhookData) {

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

}
