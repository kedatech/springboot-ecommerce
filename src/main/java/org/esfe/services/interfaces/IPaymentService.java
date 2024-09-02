package org.esfe.services.interfaces;


import org.esfe.models.Order;
import org.esfe.models.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface IPaymentService {
    Page<Payment> buscarTodosPaginados(Pageable pageable);

    List<Payment> obtenerTodos();

    Optional<Payment> buscarPorId(Integer id);

    Payment createOEditar(Payment payment);

    void eliminarPorId(Integer id);
    
    Optional<Payment> findByOrder(Order order);
}
