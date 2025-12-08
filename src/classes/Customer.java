package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;

public class Customer extends Person implements Serializable {
    private static List<Customer> extent = new ArrayList<>();
    private double loyaltyPoints;
    public HashSet<Table> tablesTaken;

    public Customer(){}

    public Customer(String name, String surname, String phoneNumber, String street, String city, String postalCode, String country, String email) {
        super(name, surname, phoneNumber, street, city, postalCode, country, email);
        loyaltyPoints = 0;
        addExtent(this);
    }

    private void updateLoyaltyPoints(double newPoints){
        loyaltyPoints += newPoints;
    }

    public static void addExtent(Customer customer){
        if(customer == null){
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if(extent.contains(customer)){
            throw new IllegalArgumentException("Such customer is already in data base");
        }
        extent.add(customer);
    }

    public void addTable(Table table) throws Exception {
        if (table == null) {
            throw new IllegalArgumentException("Table cannot be null");
        }
        if(!tablesTaken.contains(table)) {
            tablesTaken.add(table);
            if(table.getCustomer() != this){
                table.addCustomer(this);
            }
        }
    }

    public void removeTable(Table table) throws Exception {
        if (table == null) {
            throw new Exception("Table cannot be null");
        }

        if (tablesTaken.remove(table)) {
            if (table.getCustomer() == this) {
                table.removeCustomer(this);
            }
        }
    }

    public HashSet<Table> getTablesTaken() {
        return tablesTaken;
    }

    public static List<Customer> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Customer customer) {
        extent.remove(customer);
    }

    public static void writeExtent(XMLEncoder out) throws IOException {
        out.writeObject(extent);
    }

    public static void readExtent(XMLDecoder in) throws IOException, ClassNotFoundException {
        extent = (List<Customer>) in.readObject();
    }

    public double getLoyaltyPoints() {
        return loyaltyPoints;
    }


    public void displayCustomerInfo(){
        System.out.println("Name: "+this.getName());
        System.out.println("Surname: "+this.getSurname());
        System.out.println("Phone number: "+this.getPhoneNumber());
        System.out.println("Address: "+this.getAddress());
        System.out.println("Email: "+this.getEmail());
        System.out.println("Loyalty points: "+this.loyaltyPoints);
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
