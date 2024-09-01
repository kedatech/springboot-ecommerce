package org.esfe.repository;

import org.esfe.models.Order;
import org.esfe.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface IOrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByUser(User user);
}
