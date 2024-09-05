import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Data
@RequiredArgsConstructor
public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();

    public List<Order> getOrdersByStatus(OrderStatus requiredStatus) {
        return orderRepo.getOrders().stream()
                .filter(order -> order.status() == requiredStatus)
                .collect(Collectors.toList());
    }


    public Order addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Product productToOrder = productRepo.getProductById(productId);
            if (productToOrder == null) {
                System.out.println("Product with ID " + productId + " could not be ordered.");
                return null;
            }
            products.add(productToOrder);
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products,
                OrderStatus.PROCESSING);

        return orderRepo.addOrder(newOrder);
    }
}
