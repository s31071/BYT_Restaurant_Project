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
    public Double weight;
    public Category category;

    public Product(long ID, String name, Double weight, Category category, LocalDate expiryDate) {
        setID(ID);
        setName(name);
        setWeight(weight);
        setCategory(category);
        setExpiryDate(expiryDate);
        addExtent(this);
    }

    public Product(long ID, String name, Double weight, Category category) {
        this(ID, name, weight, category, null);
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
            //zakładamy, że nie dodajemy przeterminowanych produktów
            throw new IllegalArgumentException("Expiry date cannot be in the past");
        }
        this.expiryDate = expiryDate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        if (weight == null) {
            throw new IllegalArgumentException("Weight cannot be null");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
        this.weight = weight;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void remove() {
        extent.remove(this);
    }

    public static void addExtent(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        extent.add(product);
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
}