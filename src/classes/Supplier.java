package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;

public class Supplier extends Person implements Serializable {
    private static List<Supplier> extent = new ArrayList<>();

    private String companyName;
    private Category category;
    private double deliveryCost;

    public Supplier(String name, String surname, String phoneNumber, String street, String city, String postalCode, String country, String email, String companyName, Category category, double deliveryCost) {
        super(name, surname, phoneNumber, street, city, postalCode, country, email);
        setCompanyName(companyName);
        setCategory(category);
        setDeliveryCost(deliveryCost);
        addExtent(this);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }
        this.companyName = companyName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if(category == null){
            throw new IllegalArgumentException("Category cannot be null");
        }
        this.category = category;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(double deliveryCost) {
        if(deliveryCost < 0){
            throw new IllegalArgumentException("Delivery cost cannot be negative");
        }
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

    public static void writeExtent(XMLEncoder objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(XMLDecoder objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Supplier>) objectInputStream.readObject();
    }
}