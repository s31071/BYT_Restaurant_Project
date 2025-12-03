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
    private Supplier supplier;

    public ProductOrder(){}
    public ProductOrder(List<Product> products) {
        setProducts(products);
        setTotalWeight();
        setTotalSum();
        addExtent(this);
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
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

    public static void addExtent(ProductOrder productOrder) {
        if(productOrder == null){
            throw new IllegalArgumentException("ProductOrder cannot be null");
        }
        if(extent.contains(productOrder)){
            throw new IllegalArgumentException("Such productOrder is already in data base");
        }
        extent.add(productOrder);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
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

    public static void clearExtent(){
        extent.clear();
    }
}