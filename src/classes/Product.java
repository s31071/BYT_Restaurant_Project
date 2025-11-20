package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;

public class Product implements Serializable {
    private static List<Product> extent = new ArrayList<>();

    public long ID;
    public String name;
    public LocalDate expiryDate; //nullable
    public double weight;
    public Category category;
    public double price;

    public Product(long ID, String name, double weight, Category category, LocalDate expiryDate, double price) {
        setID(ID);
        setName(name);
        setWeight(weight);
        setCategory(category);
        setExpiryDate(expiryDate);
        setPrice(price);
        addExtent(this);
    }

    public Product(long ID, String name, double weight, Category category, LocalDate expiryDate) {
        this(ID, name, weight, category, expiryDate, 0.0);
    }

    public Product(long ID, String name, double weight, Category category) {
        this(ID, name, weight, category, null, 0.0);
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        if (ID <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot empty");
        }
        this.name = name;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiry date cannot be in the past");
        }
        this.expiryDate = expiryDate;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
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
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        this.category = category;
    }

    public void remove() {
        extent.remove(this);
    }

    public static void addExtent(Product newProduct) {
        if (newProduct == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        for (Product existingProduct : extent) {
            boolean sameName = existingProduct.name.equals(newProduct.name);

            boolean sameExpiry = (existingProduct.expiryDate == null && newProduct.expiryDate == null)
                    || (existingProduct.expiryDate != null && existingProduct.expiryDate.equals(newProduct.expiryDate));

            boolean sameWeight = existingProduct.weight == newProduct.weight;
            boolean sameCategory = existingProduct.category == newProduct.category;
            boolean samePrice = existingProduct.price == newProduct.price;

            if (sameName && sameExpiry && sameWeight && sameCategory && samePrice) {
                throw new IllegalArgumentException("This product already exists.");
            }
        }

        extent.add(newProduct);
    }

    public static List<Product> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Product product) {
        extent.remove(product);
    }

    public static void writeExtent(XMLEncoder objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(XMLDecoder objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Product>) objectInputStream.readObject();
    }

    public static void clearExtent(){
        extent.clear();
    }
}