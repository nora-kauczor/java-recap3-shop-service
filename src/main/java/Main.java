import java.lang.reflect.Array;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws Exception {
        ShopService myShopService = new ShopService();
        Product lamp = new Product(IdService.generateId(), "Lamp");
        Product chair = new Product(IdService.generateId(), "Chair");
        Product mug = new Product(IdService.generateId(), "Mug");
        Product blanket = new Product(IdService.generateId(), "Blanket");
        Product clock = new Product(IdService.generateId(), "Clock");
        Order order1 = new Order(IdService.generateId(), new ArrayList<>(
                Arrays.asList(lamp, chair)), OrderStatus.PROCESSING, ZonedDateTime.now());
        myShopService.getOrderRepo().addOrder(order1);
        myShopService.addOrder(new ArrayList<>(Arrays.asList(blanket.id())));
        myShopService.addOrder(new ArrayList<>(Arrays.asList(blanket.id(), clock.id())));
    }

}
