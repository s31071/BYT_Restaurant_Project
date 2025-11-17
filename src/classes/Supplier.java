package classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class Supplier extends Person implements Serializable {
    private static List<Supplier> extent = new ArrayList<>();

    private String companyName;
    private Category category;
    private double deliveryCost;

    public Supplier(String name, String surname, String phoneNumber, String address, String email, String companyName, Category category, double deliveryCost) {
        super(name, surname, phoneNumber, address, email);
        this.companyName = validateCompanyName(companyName);
        this.category = category;
        this.deliveryCost = deliveryCost;
        addExtent(this);
    }

    private String validateCompanyName(String companyName){
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }
        return companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public static void addExtent(Supplier supplier){
        if(supplier == null){
            throw new IllegalArgumentException("Supplier cannot be null");
        }
        extent.add(supplier);
    }

    public static List<Supplier> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Supplier supplier) {
        extent.remove(supplier);
    }

    public static void writeExtent(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Supplier>) objectInputStream.readObject();
    }
}