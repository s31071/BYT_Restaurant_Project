package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;

public class Supplier implements ISupplier, Serializable {
    private static List<Supplier> extent = new ArrayList<>();
    private String companyName;
    private Category category;
    private double deliveryCost;
    private HashSet<ProductOrder> productOrders;

    private Person person;

    public Supplier(){}
    public Supplier(Person person, String companyName, Category category, double deliveryCost) throws Exception{

        setPerson(person);
        setCompanyName(companyName);
        setCategory(category);
        setDeliveryCost(deliveryCost);
        productOrders = new HashSet<>();
        addExtent(this);
    }
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) throws Exception {
        if (person == null) {
            throw new Exception("Person cannot be null");
        }
        this.person = person;
    }

    public String getName() {
        return person.getName();
    }

    public void setName(String name) {
        person.setName(name);
    }

    public String getSurname() {
        return person.getSurname();
    }

    public void setSurname(String surname) {
        person.setSurname(surname);
    }

    public String getPhoneNumber() {
        return person.getPhoneNumber();
    }

    public void setPhoneNumber(String phoneNumber) {
        person.setPhoneNumber(phoneNumber);
    }

    public Address getAddress() {
        return person.getAddress();
    }

    public void setAddress(Address address) {
        person.setAddress(address);
    }

    public String getEmail() {
        return person.getEmail();
    }

    public void setEmail(String email) {
        person.setEmail(email);
    }






    public void addOrderedProduct(ProductOrder productOrder) throws Exception {
        if(productOrder == null){
            throw new Exception("Product order cannot be null");
        }
        if(!productOrders.contains(productOrder)){
            productOrders.add(productOrder);
            if(productOrder.getSupplier() != this) {
                productOrder.addSupplier(this);
            }
        }
    }

    public void removeOrderedProduct(ProductOrder productOrder) throws Exception {
        if (productOrder == null) {
            throw new Exception("Product order cannot be null");
        }

        if (productOrders.remove(productOrder)) {
            if (productOrder.getSupplier() == this) {
                productOrder.removeSupplier(this);
                ProductOrder.removeFromExtent(productOrder);
            }
        }
    }

    public HashSet<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(HashSet<ProductOrder> productOrders) {
        this.productOrders = productOrders;
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

    public static void writeExtent(XMLEncoder out) throws IOException {
        out.writeObject(extent);
    }

    public static void readExtent(XMLDecoder in) throws IOException, ClassNotFoundException {
        extent = (List<Supplier>) in.readObject();
    }

    public static void clearExtent(){
        extent.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supplier supplier = (Supplier) o;
        return person.equals(supplier.person);
    }

    @Override
    public int hashCode() {
        return person.hashCode();
    }
}