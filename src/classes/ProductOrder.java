package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductOrder implements Serializable {
    private static List<ProductOrder> extent = new ArrayList<>();
    //potrzebuje dodatkowej listy, żeby przechowywać co jest w tej konkretnej relacji
    private List<Product> products = new ArrayList<>();
    private double totalWeight;
    private double totalSum;

    public ProductOrder(List<Product> products) {
        setProducts(products);
        setTotalWeight();
        setTotalSum();
        addExtent(this);
    }

    private void setTotalSum() {
        this.totalSum = Math.round(
                products.stream()
                        .mapToDouble(Product::getPrice)
                        .sum() * 100.0
        ) / 100.0;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public void setProducts(List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Product list cannot be empty");
        }
        for (Product p : products) {
            if (p == null) {
                throw new IllegalArgumentException("Product cannot be null");
            }
        }
        this.products = new ArrayList<>(products);
        setTotalWeight();
        setTotalSum();
    }

    public List<Product> getProducts() {
        return products;
    }

    private void setTotalWeight() {
        this.totalWeight = Math.round(
                products.stream()
                        .mapToDouble(Product::getWeight)
                        .sum() * 100.0
        ) / 100.0;
    }

    public static void setExtent(List<ProductOrder> extent) {
        ProductOrder.extent = extent;
    }



    public static void addExtent(ProductOrder newProductOrder) {
        if (newProductOrder == null) {
            throw new IllegalArgumentException("ProductOrder cannot be null");
        }

        for (ProductOrder existingProductOrder : extent) {
            if (existingProductOrder.products.size() == newProductOrder.products.size()) {

                boolean identical = true;

                for (int i = 0; i < existingProductOrder.products.size(); i++) {
                    Product p1 = existingProductOrder.products.get(i);
                    Product p2 = newProductOrder.products.get(i);

                    boolean sameName = p1.name.equals(p2.name);

                    boolean sameExpiry = (p1.expiryDate == null && p2.expiryDate == null)
                            || (p1.expiryDate != null && p1.expiryDate.equals(p2.expiryDate));

                    boolean sameWeight = p1.weight == p2.weight;
                    boolean sameCategory = p1.category == p2.category;
                    boolean samePrice = p1.price == p2.price;

                    if (!(sameName && sameExpiry && sameWeight && sameCategory && samePrice)) {
                        identical = false;
                        break;
                    }
                }

                if (identical) {
                    throw new IllegalArgumentException("This ProductOrder already exists in extent");
                }
            }
        }

        extent.add(newProductOrder);
    }

    public static List<ProductOrder> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(ProductOrder productOrder) {
        extent.remove(productOrder);
    }

    public static void writeExtent(XMLEncoder out) throws java.io.IOException {
        out.writeObject(extent);
    }

    public static void readExtent(XMLDecoder in) throws java.io.IOException, ClassNotFoundException {
        extent = (List<ProductOrder>) in.readObject();
    }
}