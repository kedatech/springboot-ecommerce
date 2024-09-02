package org.esfe.repository;

import java.util.Optional;

import org.esfe.models.Order;
import org.esfe.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IPaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByOrder(Order order);
}
