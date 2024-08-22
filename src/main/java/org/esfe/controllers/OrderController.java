package org.esfe.controllers;

import org.esfe.models.Order;
import org.esfe.models.OrderItem;
import org.esfe.schemas.order.CreateOrderSchema;
import org.esfe.services.implementations.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // POST: Crear una orden
    @PostMapping
    @ResponseBody
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderSchema model) {
        Order newOrder = orderService.createOrder(model.getUser(), model.getOrderItems());
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    // DELETE: Eliminar una orden
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.eliminarPorId(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET (por usuario): Obtener órdenes del usuario y dirigir a una vista
    @GetMapping("/user/{userId}")
    public String getOrdersByUser(@PathVariable Integer userId, Model model) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        model.addAttribute("orders", orders);
        return "orders/userOrders";  // Cambia "userOrdersView" por el nombre de tu vista
    }

    // GET (detalles): Obtener detalles de una orden de un ítem
    @GetMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderItem> getOrderItemDetails(@PathVariable Integer orderId, @PathVariable Integer itemId) {
        Optional<OrderItem> orderItem = orderService.getOrderItemDetails(orderId, itemId);
        return orderItem
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    // POST (ítem): Añadir un ítem a la orden
    @PostMapping("/{orderId}/items")
    @ResponseBody
    public ResponseEntity<OrderItem> addItemToOrder(@PathVariable Integer orderId, @RequestBody OrderItem orderItem) {
        OrderItem newItem = orderService.addItemToOrder(orderId, orderItem);
        return new ResponseEntity<>(newItem, HttpStatus.CREATED);
    }

    // DELETE (ítem): Eliminar un ítem de la orden
    @DeleteMapping("/{orderId}/items/{itemId}")
    @ResponseBody
    public ResponseEntity<Void> deleteItemFromOrder(@PathVariable Integer orderId, @PathVariable Integer itemId) {
        boolean isRemoved = orderService.deleteItemFromOrder(orderId, itemId);
        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // UPDATE (ítem): Actualizar un ítem de la orden
    @PutMapping("/{orderId}/items/{itemId}")
    @ResponseBody
    public ResponseEntity<OrderItem> updateItemInOrder(@PathVariable Integer orderId, @PathVariable Integer itemId, @RequestBody OrderItem updatedItem) {
        OrderItem orderItem = orderService.updateItemInOrder(orderId, itemId, updatedItem);
        return new ResponseEntity<>(orderItem, HttpStatus.OK);
    }
}

