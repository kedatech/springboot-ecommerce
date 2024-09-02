package org.esfe.services.implementations;

import org.esfe.models.*;
import org.esfe.models.dtos.order.ReturnCreateOrder;
import org.esfe.models.enums.OrderStatus;
import org.esfe.repository.IOrderItemRepository;
import org.esfe.repository.IOrderRepository;
import org.esfe.repository.IUserRepository;
import org.esfe.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderItemRepository orderItemRepository;

    @Autowired
    private IProductService productService;

    @Autowired
    private IOrderItemService orderItemService;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private IWompiService wompiService;


    @Override
    public boolean deleteItemFromOrder(Integer orderId, Integer itemId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            List<OrderItem> orderItems = order.getOrderItems();

            // Encuentra el ítem a eliminar
            Optional<OrderItem> itemToDeleteOptional = orderItems.stream()
                    .filter(item -> Integer.valueOf(item.getId()).equals(itemId))
                    .findFirst();

            if (itemToDeleteOptional.isPresent()) {
                OrderItem itemToDelete = itemToDeleteOptional.get();

                // Elimina el ítem de la orden y actualiza el total
                orderItems.remove(itemToDelete);
                order.setOrderItems(orderItems);
                order.setTotalAmount(order.getTotalAmount() - (itemToDelete.getPrice() * itemToDelete.getQuantity()));

                // Guarda la orden actualizada
                orderRepository.save(order);
                orderItemRepository.deleteById(itemId);
                return true;
            }
        }

        return false;
    }

    public Order createOrder(User user, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems (orderItems);
        order.setStatus(OrderStatus.PENDING);

        double totalAmount = 0;
        for (OrderItem orderItem : orderItems) {
            totalAmount += orderItem.getPrice() * orderItem.getQuantity();
        }

        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    public ReturnCreateOrder createOrderMap(User user, List<Map<String, Object>> orderItems) {
        List<OrderItem> orderItemList = new ArrayList<>();
        double totalAmount = 0;
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(0);
        Order orderSaved = orderRepository.save(order);

        for (Map<String, Object> itemData : orderItems){
            String productIdStr = (String) itemData.get("id");
            Integer productId = Integer.parseInt(productIdStr);
            Integer quantity = (Integer) itemData.get("quantity");
            Optional<Product> productOpt = productService.buscarPorId(productId);

            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                double price = product.getPrice();
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(orderSaved);
                orderItem.setProduct(product);
                orderItem.setQuantity(quantity);
                orderItem.setPrice(price);
                orderItem.setActive(true);

                orderItemList.add(orderItem);
                orderItemService.createOEditar(orderItem);

                totalAmount += price * quantity;
                product.setStock(Math.max(product.getStock() - quantity, 0));
                productService.createOEditar(product);
            }
        }

        orderSaved.setTotalAmount(totalAmount);
        orderSaved.setOrderItems (orderItemList);


        Order theOrder = orderRepository.save(orderSaved);

        var paymentLinkRequest = wompiService.generateLink(theOrder.getTotalAmount(), theOrder.getId());

        Payment payment = new Payment();
        payment.setAmount(theOrder.getTotalAmount());
        payment.setOrder(theOrder);
        payment.setUrlEnlace(paymentLinkRequest.getUrlEnlace());
        payment.setIdEnlace(paymentLinkRequest.getIdEnlace());
        payment.setUrlQrCodeEnlace(paymentLinkRequest.getUrlQrCodeEnlace());
        payment.setStatus("PENDING");
        paymentService.createOEditar(payment);

        ReturnCreateOrder returnCreateOrder = new ReturnCreateOrder();
        returnCreateOrder.setOrder(theOrder);
        returnCreateOrder.setPayment(payment);

        return returnCreateOrder;
    }


    @Override
    public List<Order> getOrdersByUser(Integer userId) {
        // Obtener todas las órdenes
        List<Order> allOrders = orderRepository.findAll();

        // Filtrar órdenes que pertenecen al usuario con el userId dado
        List<Order> userOrders = allOrders.stream()
                .filter(order -> order.getUser().getId() == userId)  // Compara valores primitivos
                .collect(Collectors.toList());

        return userOrders;
    }

    @Override
    public Optional<OrderItem> getOrderItemDetails(Integer orderId, Integer itemId) {
        // Buscar la orden por ID
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            // Filtrar el ítem dentro de la orden
            return order.get().getOrderItems().stream()
                    .filter(item -> item.getId() == itemId)  // Compara con el valor primitivo
                    .findFirst();
        }

        return Optional.empty(); // Retornar vacío si la orden no existe o el ítem no se encuentra
    }

    @Override
    public OrderItem addItemToOrder(Integer orderId, OrderItem orderItem) {
        // Buscar la orden por ID
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            // Agregar el ítem a la orden
            Order existingOrder = order.get();
            existingOrder.getOrderItems().add(orderItem);
            orderItem.setOrder(existingOrder); // Asocia el ítem con la orden

            // Guardar la orden actualizada y el ítem
            orderRepository.save(existingOrder);
            orderItemRepository.save(orderItem);

            return orderItem;
        }

        return null; // Retornar nulo si la orden no existe
    }


    @Override
    public OrderItem updateItemInOrder(Integer orderId, Integer itemId, OrderItem updatedItem) {
        // Implementación del método
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            Order existingOrder = order.get();

            Optional<OrderItem> itemToUpdate = existingOrder.getOrderItems().stream()
                    .filter(item -> item.getId() == itemId)  // Compara con el valor primitivo
                    .findFirst();

            if (itemToUpdate.isPresent()) {
                OrderItem item = itemToUpdate.get();

                // Actualiza los campos del item
                item.setProduct(updatedItem.getProduct());
                item.setPrice(updatedItem.getPrice());
                item.setQuantity(updatedItem.getQuantity());

                orderItemRepository.save(item);
                return item;
            }
        }

        return null; // Retornar nulo si la orden no existe o el ítem no se encuentra
    }




    @Override
    public Page<Order> buscarTodosPaginados(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public List<Order> obtenerTodos() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> buscarPorId(Integer id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order createOEditar(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void eliminarPorId(Integer id) {
        orderRepository.deleteById(id);
    }
}
