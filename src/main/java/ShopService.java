import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();

    public Map<OrderStatus, Order> getOldestOrderPerStatus() {
        List<Order> inDeliveryOrders = null;
        List<Order> processingOrders = null;
        List<Order> completedOrders = null;
        try {
            inDeliveryOrders = getOrdersByStatus(OrderStatus.IN_DELIVERY);
            processingOrders = getOrdersByStatus(OrderStatus.PROCESSING);
            completedOrders = getOrdersByStatus(OrderStatus.COMPLETED);
        } catch (Exception exception) {
        }
        Order oldestOrderInDelivery = null;
        Order oldestProcessedOrder = null;
        Order oldestCompletedOrder = null;
        try {
            oldestOrderInDelivery = getOldestOrder(inDeliveryOrders);
            oldestProcessedOrder = getOldestOrder(processingOrders);
            oldestCompletedOrder = getOldestOrder(completedOrders);
        } catch (Exception exception) {
        }
        Map<OrderStatus, Order> oldestOrdersPerStatus = new HashMap<>();
        oldestOrdersPerStatus.put(OrderStatus.COMPLETED, oldestCompletedOrder);
        oldestOrdersPerStatus.put(OrderStatus.IN_DELIVERY, oldestOrderInDelivery);
        oldestOrdersPerStatus.put(OrderStatus.PROCESSING, oldestProcessedOrder);
        return oldestOrdersPerStatus;
    }

    public static Order getOldestOrder(List<Order> orders) throws Exception {
        if (orders == null) {
            throw new Exception("Couldn't get oldest order of list because orders is null.");
        }
        List<ZonedDateTime> timeStamps = orders.stream().map(order -> order.ordered()).collect(Collectors.toList());
        ZonedDateTime oldest = Collections.min(timeStamps);
        return orders.stream().filter(order -> order.ordered().equals(oldest)).collect(Collectors.toList()).get(0);
    }

    public Order updateOrder(String orderId, OrderStatus newStatus) throws Exception {
        if (orderRepo.getOrderById(orderId) == null) {
            throw new Exception("Couldn't update order because an order with " +
                    "the given ID doesn't exist.");
        }
        return orderRepo.getOrderById(orderId).withStatus(newStatus);
    }

    public List<Order> getOrdersByStatus(OrderStatus requiredStatus) throws Exception {
        if (orderRepo.getOrders() == null) {
            throw new Exception("Couldn't get orders with specific status because orders is null.");
        }
        return orderRepo.getOrders().stream()
                .filter(order -> order.status() == requiredStatus)
                .collect(Collectors.toList());
    }


    public Order addOrder(List<String> productIds) throws Exception {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrderOptional = productRepo.getProductById(productId);
            Product productToOrder = productToOrderOptional.orElse(null);
            if (productToOrder == null) {
                throw new Exception("Product with ID " + productId + " could not be ordered.");
            }
            products.add(productToOrder);
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products,
                OrderStatus.PROCESSING, ZonedDateTime.now());

        return orderRepo.addOrder(newOrder);
    }
}
