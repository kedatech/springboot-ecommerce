package org.esfe.controllers;

import net.minidev.json.parser.JSONParser;
import org.esfe.models.Order;
import org.esfe.models.OrderItem;
import org.esfe.models.Product;
import org.esfe.models.User;
import org.esfe.models.enums.OrderStatus;
import org.esfe.services.implementations.ProductService;
import org.esfe.services.interfaces.IOrderItemService;
import org.esfe.services.interfaces.IOrderService;
import org.esfe.services.interfaces.IProductService;
import org.esfe.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
}

