package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.io.IOException;

public class Product implements Serializable {

    private static List<Product> extent = new ArrayList<>();

    private long ID;
    private String name;
    private LocalDate expiryDate;
    private double weight;
    private Category category;
    private double price;

    // many-to-many association Product - ProductOrder (1..* , 1..*)
    private HashSet<ProductOrder> productOrders = new HashSet<>();

    public Product() {}

    public Product(long ID, String name, double weight,
                   Category category, LocalDate expiryDate, double price) {

        setID(ID);
        setName(name);
        setWeight(weight);
        setCategory(category);
        setExpiryDate(expiryDate);
        setPrice(price);

        addExtent(this);
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        if (ID <= 0)
            throw new IllegalArgumentException("Product ID must be positive");
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Product name cannot be empty");
        this.name = name;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        if (expiryDate != null && expiryDate.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Expiry date cannot be in the past");
        this.expiryDate = expiryDate;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight <= 0)
            throw new IllegalArgumentException("Weight must be positive");
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category == null)
            throw new IllegalArgumentException("Category cannot be null");
        this.category = category;
    }

    public HashSet<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public void addProductOrder(ProductOrder po) {
        if (po == null)
            throw new IllegalArgumentException("ProductOrder cannot be null");

        if (productOrders.contains(po))
            return;

        productOrders.add(po);

        if (!po.getProducts().contains(this)) {
            po.addProduct(this);
        }
    }

    public void removeProductOrder(ProductOrder po) {
        if (po == null)
            throw new IllegalArgumentException("ProductOrder cannot be null");

        if (!productOrders.contains(po))
            return;

        if (productOrders.size() == 1)
            throw new IllegalStateException(
                    "Cannot remove last ProductOrder; Product must belong to at least one ProductOrder");

        productOrders.remove(po);

        if (po.getProducts().contains(this)) {
            po.removeProduct(this);
        }
    }

    public static void addExtent(Product product) {
        if (product == null)
            throw new IllegalArgumentException("Product cannot be null");
        if (extent.contains(product))
            throw new IllegalArgumentException("Such product already exists in system");
        extent.add(product);
    }

    public static List<Product> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Product product) {
        extent.remove(product);
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static void writeExtent(XMLEncoder out) throws IOException {
        out.writeObject(extent);
    }

    public static void readExtent(XMLDecoder in) throws IOException, ClassNotFoundException {
        extent = (List<Product>) in.readObject();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return ID == product.ID && Double.compare(weight, product.weight) == 0 && Double.compare(price, product.price) == 0 && Objects.equals(name, product.name) && Objects.equals(expiryDate, product.expiryDate) && category == product.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, name, expiryDate, weight, category, price);
    }
}