package org.esfe.models.dtos.wompi;

import lombok.Data;
import org.esfe.models.OrderItem;
import java.util.List;

@Data
public class GeneratePayment {
    private double amount;
    private List<OrderItem> items;
}
