package org.esfe.services.interfaces;


import org.esfe.models.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IOrderItemService {
    Page<OrderItem> buscarTodosPaginados(Pageable pageable);

    List<OrderItem> obtenerTodos();

    Optional<OrderItem> buscarPorId(Integer id);

    OrderItem createOEditar(OrderItem orderItem);

    void eliminarPorId(Integer id);
}
