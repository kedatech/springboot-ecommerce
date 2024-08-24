package org.esfe.services.implementations;

import org.esfe.models.Order;
import org.esfe.models.OrderItem;
import org.esfe.models.User;
import org.esfe.models.enums.OrderStatus;
import org.esfe.repository.IOrderItemRepository;
import org.esfe.repository.IOrderRepository;
import org.esfe.repository.IUserRepository;
import org.esfe.services.interfaces.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    private IOrderItemRepository orderItemRepository;

    public Order createOrder(User user, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems (orderItems);
        order.setStatus(OrderStatus.PENDING);

        double totalAmount = 0;
        for (OrderItem orderItem : orderItems) {
            totalAmount += orderItem.getPrice() * orderItem.getQuantity();
        }

        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }


    @Override
    public List<Order> getOrdersByUser(Integer userId) {
        // Obtener todas las órdenes
        List<Order> allOrders = orderRepository.findAll();

        // Filtrar órdenes que pertenecen al usuario con el userId dado
        List<Order> userOrders = allOrders.stream()
                .filter(order -> order.getUser().getId() == userId)  // Compara valores primitivos
                .collect(Collectors.toList());

        return userOrders;
    }

    @Override
    public Optional<OrderItem> getOrderItemDetails(Integer orderId, Integer itemId) {
        // Buscar la orden por ID
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            // Filtrar el ítem dentro de la orden
            return order.get().getOrderItems().stream()
                    .filter(item -> item.getId() == itemId)  // Compara con el valor primitivo
                    .findFirst();
        }

        return Optional.empty(); // Retornar vacío si la orden no existe o el ítem no se encuentra
    }

    @Override
    public OrderItem addItemToOrder(Integer orderId, OrderItem orderItem) {
        // Buscar la orden por ID
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            // Agregar el ítem a la orden
            Order existingOrder = order.get();
            existingOrder.getOrderItems().add(orderItem);
            orderItem.setOrder(existingOrder); // Asocia el ítem con la orden

            // Guardar la orden actualizada y el ítem
            orderRepository.save(existingOrder);
            orderItemRepository.save(orderItem);

            return orderItem;
        }

        return null; // Retornar nulo si la orden no existe
    }

    @Override
    public boolean deleteItemFromOrder(Integer orderId, Integer itemId) {
        // Buscar la orden por ID
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            Order existingOrder = order.get();
            // Buscar el ítem dentro de la orden
            Optional<OrderItem> itemToRemove = existingOrder.getOrderItems().stream()
                    .filter(item -> item.getId() == itemId)  // Compara con el valor primitivo
                    .findFirst();

            if (itemToRemove.isPresent()) {
                existingOrder.getOrderItems().remove(itemToRemove.get());
                orderItemRepository.delete(itemToRemove.get());
                orderRepository.save(existingOrder);
                return true;
            }
        }

        return false; // Retorna false si la orden no existe o el ítem no se encuentra
    }


    // @Override
    // public OrderItem updateItemInOrder(Integer orderId, Integer itemId, OrderItem updatedItem) {
    //     // Buscar la orden por ID
    //     Optional<Order> order = orderRepository.findById(orderId);

    //     if (order.isPresent()) {
    //         Order existingOrder = order.get();

    //         // Buscar el ítem dentro de la orden
    //         Optional<OrderItem> itemToUpdate = existingOrder.getOrderItems().stream()
    //                 .filter(item -> item.getId().equals(itemId))
    //                 .findFirst();

    //         if (itemToUpdate.isPresent()) {
    //             OrderItem item = itemToUpdate.get();

    //             // Actualizar el ítem con los nuevos valores
    //             item.setame(updatedItem.getName());
    //             item.setPrice(updatedItem.getPrice());
    //             item.setQuantity(updatedItem.getQuantity());
    //             // Puedes actualizar otros campos según tu entidad OrderItem

    //             // Guardar el ítem actualizado
    //             orderItemRepository.save(item);
    //             return item;
    //         }
    //     }

    //     throw new ResourceNotFoundException("Order or Item not found"); // Lanza una excepción si no se encuentra la orden o el ítem
    // }

    @Override
    public Page<Order> buscarTodosPaginados(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public List<Order> obtenerTodos() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> buscarPorId(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOEditar(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void eliminarPorId(Integer id) {
        orderRepository.deleteById(id);
    }
}
