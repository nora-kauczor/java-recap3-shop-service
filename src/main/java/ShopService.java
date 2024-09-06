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
        Order oldestOrderInDelivery = getOldestOrderOfStatus(OrderStatus.IN_DELIVERY);
        Order oldestProcessedOrder = getOldestOrderOfStatus(OrderStatus.PROCESSING);
        Order oldestCompletedOrder = getOldestOrderOfStatus(OrderStatus.COMPLETED);
        Map<OrderStatus, Order> oldestOrdersPerStatus = new HashMap<>();
        oldestOrdersPerStatus.put(OrderStatus.COMPLETED, oldestCompletedOrder);
        oldestOrdersPerStatus.put(OrderStatus.IN_DELIVERY, oldestOrderInDelivery);
        oldestOrdersPerStatus.put(OrderStatus.PROCESSING, oldestProcessedOrder);
        return oldestOrdersPerStatus;
    }

    public Order getOldestOrderOfStatus(OrderStatus status) {
        List<Order> ordersOfStatus = getOrdersByStatus(status);
        return getOldestOrder(ordersOfStatus);
    }

    public Order getOldestOrder(List<Order> orders) {
        List<ZonedDateTime> timeStamps = orders.stream().map(order -> order.ordered())
                .collect(Collectors.toList());
        ZonedDateTime oldest = Collections.min(timeStamps);
        return orders.stream().filter(order -> order.ordered().equals(oldest))
                .collect(Collectors.toList()).get(0);
    }

    public List<Order> getOrdersByStatus(OrderStatus requiredStatus) {
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
            Product productToOrder = productToOrderOptional.orElse(null); // TODO
            if (productToOrder == null) {
                throw new ProductDoesntExistException("Product with ID " + productId + " could not be ordered.");
            }
            orderedProducts.add(productToOrder);
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), orderedProducts,
                OrderStatus.PROCESSING, ZonedDateTime.now());

        return orderRepo.addOrder(newOrder);
    }
}
