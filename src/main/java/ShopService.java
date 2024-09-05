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

    public Map<String, Order> getOldestOrderPerStatus(OrderStatus status) {
        List<Order> inDeliveryOrders = getOrdersByStatus(OrderStatus.IN_DELIVERY);
        List<Order> processingOrders = getOrdersByStatus(OrderStatus.PROCESSING);
        List<Order> completedOrders = getOrdersByStatus(OrderStatus.COMPLETED);
        Order oldestOrderInDelivery = getOldestOrder(inDeliveryOrders);
        Order oldestProcessedOrder = getOldestOrder(processingOrders);
        Order oldestCompletedOrder = getOldestOrder(completedOrders);
return new HashMap<>(Map.ofEntries(Map.entry(oldestProcessedOrder.id(), oldestCompletedOrder),
        Map.entry(oldestOrderInDelivery.id(), oldestOrderInDelivery),
        Map.entry(oldestProcessedOrder.id(), oldestProcessedOrder)));
    }

    public Order getOldestOrder(List<Order> orders) {
        List<ZonedDateTime> timeStamps = orders.stream().map(order -> order.ordered()).collect(Collectors.toList());
        ZonedDateTime oldest = Collections.min(timeStamps);
        return orders.stream().filter(order -> order.ordered().equals(oldest)).collect(Collectors.toList()).getFirst();
    }

    public Order updateOrder(String orderId, OrderStatus newStatus) {
        Order order = orderRepo.getOrderById(orderId);
        return orderRepo.getOrderById(orderId).withStatus(newStatus);
    }

    public List<Order> getOrdersByStatus(OrderStatus requiredStatus) {
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

//    public Order addOrder(List<String> productIds) {
//        List<Product> products = new ArrayList<>();
//        for (String productId : productIds) {
//            Optional<Product> productToOrderOptional = productRepo.getProductById(productId);
//            Product productToOrder = productToOrderOptional.orElse(null);
//            if (productToOrder == null) {
//                System.out.println("Product with ID " + productId + " could not be ordered.");
//                return null;
//            }
//            products.add(productToOrder);
//        }
//
//        Order newOrder = new Order(UUID.randomUUID().toString(), products,
//                OrderStatus.PROCESSING);
//
//        return orderRepo.addOrder(newOrder);
//    }


}
