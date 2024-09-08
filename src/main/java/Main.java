import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;


public class Main {
    public static void main(String[] args) throws Exception {
        FileStreamer fileStreamer = new FileStreamer();
        try {
            fileStreamer.streamFile("src/transactions.txt");
        } catch (IOException e) {
            System.out.println("An IO exception occurred.");
        }


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
