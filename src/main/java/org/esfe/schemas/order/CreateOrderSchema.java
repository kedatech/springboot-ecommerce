package org.esfe.schemas.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.esfe.models.OrderItem;
import org.esfe.models.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderSchema {
    private User user;
    private List<OrderItem> orderItems;

}
