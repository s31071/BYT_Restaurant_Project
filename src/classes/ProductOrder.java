package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductOrder implements Serializable {
    private static List<ProductOrder> extent = new ArrayList<>();
//potrzebuje dodatkowejn listy, żeby przechowywać co jest w tej konkretnej relacji
    private List<Product> products = new ArrayList<>();
    private double totalWeight;

    public ProductOrder(List<Product> products) {
        setProducts(products);
        computeTotalWeight();
        addExtent(this);
    }

    public void setProducts(List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Product list cannot be empty");
        }
        this.products = new ArrayList<>(products);
        computeTotalWeight();
    }

    public List<Product> getProducts() {
        return products;
    }

    private void computeTotalWeight() {
        this.totalWeight = products.stream()
                .mapToDouble(Product::getWeight)
                .sum();
    }

    public double getWeight() {
        return totalWeight;
    }


//    public double getTotalPrice() {
//
//    }


    public static void addExtent(ProductOrder productOrder) {
        if (productOrder == null) {
            throw new IllegalArgumentException("ProductOrder cannot be null");
        }
        extent.add(productOrder);
    }

    public static List<ProductOrder> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(ProductOrder productOrder) {
        extent.remove(productOrder);
    }

    public static void writeExtent(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.writeObject(extent);
    }

    public static void readExtent(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        extent = (List<ProductOrder>) in.readObject();
    }
}