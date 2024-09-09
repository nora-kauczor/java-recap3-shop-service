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

    public Map<OrderStatus, Order> getOldestOrdersPerStatus() throws OrderDoesntExistException {
        Order oldestOrderInDelivery = getOldestOrderOfCertainStatus(OrderStatus.IN_DELIVERY);
        Order oldestProcessedOrder = getOldestOrderOfCertainStatus(OrderStatus.PROCESSING);
        Order oldestCompletedOrder = getOldestOrderOfCertainStatus(OrderStatus.COMPLETED);
        Map<OrderStatus, Order> oldestOrdersPerStatus = new HashMap<>();
        if (oldestCompletedOrder != null) {
            oldestOrdersPerStatus.put(OrderStatus.COMPLETED, oldestCompletedOrder);
        }
        if (oldestOrderInDelivery != null) {
            oldestOrdersPerStatus.put(OrderStatus.IN_DELIVERY, oldestOrderInDelivery);
        }
        if (oldestProcessedOrder != null) {
            oldestOrdersPerStatus.put(OrderStatus.PROCESSING, oldestProcessedOrder);
        }
        return oldestOrdersPerStatus;
    }

    public Order getOldestOrderOfCertainStatus(OrderStatus status) throws OrderDoesntExistException {
        List<Order> ordersOfStatus = getAllOrdersOfCertainStatus(status);
        return getOldestOrder(ordersOfStatus).orElse(null);
    }

    public Optional<Order> getOldestOrder(List<Order> orders) {
        return orders.stream().min(Comparator.comparing(Order::ordered));
    }

    public List<Order> getAllOrdersOfCertainStatus(OrderStatus requiredStatus) {
        return orderRepo.getOrders().stream()
                .filter(order -> order.status() == requiredStatus)
                .collect(Collectors.toList());
    }

    public Order updateOrder(String orderId, OrderStatus newStatus) throws Exception {
        if (orderRepo.getOrderById(orderId) == null) {
            throw new OrderDoesntExistException("Couldn't update order because an order with " +
                    "the given ID doesn't exist.");
        }
        return orderRepo.getOrderById(orderId).withStatus(newStatus);
    }

    public Order addOrder(List<String> productIds) throws Exception {
        List<Product> orderedProducts = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrderOptional = productRepo.getProductById(productId);
            Product productToOrder = productToOrderOptional.orElseThrow(() ->
                    new ProductDoesntExistException("Product with ID " + productId +
                            " could not be ordered."));
            orderedProducts.add(productToOrder);
        }
        Order newOrder = new Order(UUID.randomUUID().toString(), orderedProducts,
                OrderStatus.PROCESSING, ZonedDateTime.now());

        return orderRepo.addOrder(newOrder);
    }
}
