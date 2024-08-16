package org.esfe.services.implementations;

import org.esfe.models.OrderItem;
import org.esfe.repository.IOrderItemRepository;
import org.esfe.repository.IUserRepository;
import org.esfe.services.interfaces.IOrderItemService;
import org.esfe.services.interfaces.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService implements IOrderItemService {

    @Autowired
    private IOrderItemRepository orderItemRepository;

    @Override
    public Page<OrderItem> buscarTodosPaginados(Pageable pageable) {
        return orderItemRepository.findAll(pageable);
    }

    @Override
    public List<OrderItem> obtenerTodos() {
        return orderItemRepository.findAll();
    }

    @Override
    public Optional<OrderItem> buscarPorId(Integer id) {
        return orderItemRepository.findById(id);
    }

    @Override
    public OrderItem createOEditar(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void eliminarPorId(Integer id) {
        orderItemRepository.deleteById(id);
    }
}
