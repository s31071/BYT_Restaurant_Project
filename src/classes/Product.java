package classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    public long ID;
    public String name;
    public LocalDate expiryDate; //nullable
    public Double weight;
    public String category;

    private static List<Product> productList = new ArrayList<>();

    public Product(long ID, String name, Double weight, String category, LocalDate expiryDate) {
        setID(ID);
        setName(name);
        setWeight(weight);
        setCategory(category);
        setExpiryDate(expiryDate);
        productList.add(this);
    }
    public Product(long ID, String name, Double weight, String category) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        this.category = category;
    }

    public void remove() {
        productList.remove(this);
    }
    public static List<Product> getProductList() {
        return productList;
    }

}

