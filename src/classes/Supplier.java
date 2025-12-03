package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;

public class Supplier extends Person implements Serializable {
    private static List<Supplier> extent = new ArrayList<>();
    private String companyName;
    private Category category;
    private double deliveryCost;
    public HashSet<ProductOrder> productOrders;

    public Supplier(){}
    public Supplier(String name, String surname, String phoneNumber, String street, String city, String postalCode, String country, String email, String companyName, Category category, double deliveryCost) {
        super(name, surname, phoneNumber, street, city, postalCode, country, email);
        setCompanyName(companyName);
        setCategory(category);
        setDeliveryCost(deliveryCost);
        addExtent(this);
        productOrders = new HashSet<>();
    }

    public void addOrderedProduct(ProductOrder productOrder) throws Exception {
        if(productOrder == null){
            throw new Exception("Product order cannot be null");
        }
        productOrders.add(productOrder);
        productOrder.setSupplier(this);
    }

    public void removeOrderedProduct(ProductOrder productOrder) throws Exception {
        if(productOrder == null){
            throw new Exception("Product order cannot be null");
        }
        if(!productOrders.contains(productOrder)){
            throw new Exception("Given product order is not assigned to this supplier");
        }
        productOrders.remove(productOrder);
        productOrder.setSupplier(null);
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
        if(extent.contains(supplier)){
            throw new IllegalArgumentException("Such supplier is already in data base");
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

    public static void clearExtent(){
        extent.clear();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}