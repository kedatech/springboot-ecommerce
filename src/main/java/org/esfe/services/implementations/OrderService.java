package org.esfe.services.implementations;

import org.esfe.models.Order;
import org.esfe.models.OrderItem;
import org.esfe.models.User;
import org.esfe.repository.IOrderRepository;
import org.esfe.repository.IUserRepository;
import org.esfe.services.interfaces.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {
    public Order createOrder(User user, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems (orderItems);
        order.setStatus("ACTIVE");

        double totalAmount = 0;
        for (OrderItem orderItem : orderItems) {
            totalAmount += orderItem.getPrice() * orderItem.getQuantity();
        }

        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }


    @Autowired
    private IOrderRepository orderRepository;

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
