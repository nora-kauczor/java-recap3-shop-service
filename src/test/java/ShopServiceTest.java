import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void updateOrder() {
        ShopService shopService = new ShopService();
        OrderRepo repo = new OrderMapRepo();
        Product product0 = new Product("1", "Apple");
        Order newOrder0 = new Order("1", List.of(product0), OrderStatus.PROCESSING);
        shopService.setOrderRepo(repo);
        Order        expected = new Order("1", List.of(product0), OrderStatus.IN_DELIVERY);
        Order   actual= shopService.updateOrder("1", OrderStatus.IN_DELIVERY);
        assertEquals(expected, actual);
    }

    @Test
    void getOrdersByStatus() {
        ShopService shopService = new ShopService();
        OrderRepo repo = new OrderMapRepo();
        Product product0 = new Product("1", "Apple");
        Order newOrder0 = new Order("1", List.of(product0), OrderStatus.PROCESSING);
        Product product1 = new Product("2", "Banana");
        Order newOrder1 = new Order("2", List.of(product1), OrderStatus.PROCESSING);
        Product product2 = new Product("3", "Orange");
        Order newOrder2 = new Order("3", List.of(product2), OrderStatus.COMPLETED);
        Product product3 = new Product("4", "Pear");
        Order newOrder3 = new Order("4", List.of(product3), OrderStatus.IN_DELIVERY);
        repo.addOrder(newOrder0);
        repo.addOrder(newOrder1);
        repo.addOrder(newOrder2);
        repo.addOrder(newOrder3);
        shopService.setOrderRepo(repo);
        List<Order> expected = new ArrayList<>(Arrays.asList(newOrder0, newOrder1));
        List<Order> actual = shopService.getOrdersByStatus(OrderStatus.PROCESSING);
        assertEquals(expected, actual);
    }


    @Test
    void addOrderTest() throws Exception {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")),
                OrderStatus.IN_DELIVERY);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() throws Exception {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        assertNull(actual);
    }
}
