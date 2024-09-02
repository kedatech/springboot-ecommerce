package org.esfe.models.dtos.order;

import org.esfe.models.Order;
import org.esfe.models.Payment;

import lombok.Data;

@Data
public class ReturnCreateOrder {
    private Order order;
    private Payment payment;
}
