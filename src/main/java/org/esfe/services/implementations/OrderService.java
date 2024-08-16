package org.esfe.services.implementations;

import org.esfe.models.Order;
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
