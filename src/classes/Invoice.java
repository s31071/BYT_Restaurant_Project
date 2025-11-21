package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;

public class Invoice extends Payment implements Serializable {
    private static List<Invoice> extent = new ArrayList<>();

    public long ID;
    public long taxIdentificationNumber;
    public String name;
    private Address address;
    private ProductOrder productOrder;
    private double sum;

    public Invoice(){}

    public Invoice(PaymentMethod method, long ID, long taxIdentificationNumber, String name,
                   String street, String city, String postalCode, String country, ProductOrder productOrder) {
        super(method);
        setID(ID);
        setTaxIdentificationNumber(taxIdentificationNumber);
        setName(name);
        Address address = new Address(street, city, postalCode, country);
        setAddress(address);
        setProductOrder(productOrder);
        setSum();
        addExtent(this);
    }

    @Override
    public void setSum() {
        double value = 0.0;
        if (productOrder != null) {
            value = productOrder.getTotalSum();
        }
        this.sum = value;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        if (ID <= 0) {
            throw new IllegalArgumentException("Invoice ID must be positive");
        }
        this.ID = ID;
    }

    public long getTaxIdentificationNumber() {
        return taxIdentificationNumber;
    }

    public void setTaxIdentificationNumber(long taxIdentificationNumber) {
        if (taxIdentificationNumber <= 0) {
            throw new IllegalArgumentException("Tax Identification Number must be positive");
        }
        this.taxIdentificationNumber = taxIdentificationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        this.address = address;
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder productOrder) {
        if (productOrder == null) {
            throw new IllegalArgumentException("ProductOrder cannot be empty");
        }
        this.productOrder = productOrder;
        setSum();
    }

    @Override
    public double getSum() {
        return sum;
    }

    public static void addExtent(Invoice invoice) {
        if(invoice == null){
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        if(extent.contains(invoice)){
            throw new IllegalArgumentException("Such invoice is already in data base");
        }
        extent.add(invoice);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static List<Invoice> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Invoice invoice) {
        extent.remove(invoice);
    }

    public static void writeExtent(XMLEncoder objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(XMLDecoder objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Invoice>) objectInputStream.readObject();
    }

    public static void clearExtent(){
        extent.clear();
    }
}