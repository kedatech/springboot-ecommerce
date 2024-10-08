package org.esfe.services.interfaces;


import org.esfe.models.Order;
import org.esfe.models.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IOrderService {

    OrderItem updateItemInOrder(Integer orderId, Integer itemId, OrderItem updatedItem);

    boolean deleteItemFromOrder(Integer orderId, Integer itemId);

    OrderItem addItemToOrder(Integer orderId, OrderItem orderItem);

    Optional<OrderItem> getOrderItemDetails(Integer orderId, Integer itemId);

    List<Order> getOrdersByUser(Integer userId);

    Page<Order> buscarTodosPaginados(Pageable pageable);

    List<Order> obtenerTodos();

    Optional<Order> buscarPorId(Integer id);

    Order createOEditar(Order order);

    void eliminarPorId(Integer id);
}
