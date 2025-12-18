package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.*;
import java.io.Serializable;
import java.io.IOException;

public class Customer implements Icustomer, Serializable {

    private static List<Customer> extent = new ArrayList<>();

    private Person person;
    private Double loyaltyPoints;
    public HashSet<Table> tablesTaken = new HashSet<>();

    public Customer() {}

    public Customer(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        this.person = person;
        this.loyaltyPoints = 0.0;
        addExtent(this);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getName() {
        return person.getName();
    }

    public String getSurname() {
        return person.getSurname();
    }

    public String getPhoneNumber() {
        return person.getPhoneNumber();
    }

    public Address getAddress() {
        return person.getAddress();
    }

    public String getEmail() {
        return person.getEmail();
    }

    public void updateLoyaltyPoints(Double newPoints) {
        loyaltyPoints += newPoints;
    }

    public Double getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Double loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public void addTable(Table table) throws Exception {
        if (table == null) {
            throw new IllegalArgumentException("Table cannot be null");
        }
        if (!tablesTaken.contains(table)) {
            tablesTaken.add(table);
            if (table.getCustomer() != this) {
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

    public static void addExtent(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (extent.contains(customer)) {
            throw new IllegalArgumentException("Such customer is already in data base");
        }
        extent.add(customer);
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

    public static void readExtent(XMLDecoder in)
            throws IOException, ClassNotFoundException {
        extent = (List<Customer>) in.readObject();
    }

    public static void clearExtent() {
        extent.clear();
    }

    public void displayCustomerInfo() {
        System.out.println("Name: " + getName());
        System.out.println("Surname: " + getSurname());
        System.out.println("Phone number: " + getPhoneNumber());
        System.out.println("Address: " + getAddress());
        System.out.println("Email: " + getEmail());
        System.out.println("Loyalty points: " + loyaltyPoints);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return person.equals(customer.person);
    }

    @Override
    public int hashCode() {
        return person.hashCode();
    }
}
