import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepo {
    private List<Product> products;

    public ProductRepo() {
        products = new ArrayList<>();
        products.add(new Product("1", "Apple"));
    }

    public List<Product> getProducts() {
        return products;
    }
// Modify the 'getProductById' method in your ProductRepo so that it
// returns an Optional if the product exists, otherwise an empty Optional.


    public Optional<Product> getProductById(String id) {
        for (Product product : products) {
            if (product.id().equals(id)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }




//    public Product getProductById(String id) {
//        for (Product product : products) {
//            if (product.id().equals(id)) {
//                return product;
//            }
//        }
//        return null;
//    }

    public Product addProduct(Product newProduct) {
        products.add(newProduct);
        return newProduct;
    }

    public void removeProduct(String id) {
        for (Product product : products) {
           if (product.id().equals(id)) {
               products.remove(product);
               return;
           }
        }
    }
}
