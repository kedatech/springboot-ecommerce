package org.esfe.repository;

import org.esfe.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<Order, Integer> {

}
