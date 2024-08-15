package org.esfe.services.interfaces;


import org.esfe.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    Page<Order> buscarTodosPaginados(Pageable pageable);

    List<Order> obtenerTodos();

    Optional<Order> buscarPorId(Integer id);

    Order createOEditar(Order order);

    void eliminarPorId(Integer id);
}
