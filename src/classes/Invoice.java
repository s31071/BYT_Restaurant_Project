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

    public Invoice(PaymentMethod method, long ID, long taxIdentificationNumber, String name, Address address, ProductOrder productOrder) {
        super(method);
        setID(ID);
        setTaxIdentificationNumber(taxIdentificationNumber);
        setName(name);
        setAddress(address);
        setProductOrder(productOrder);
        addExtent(this);
    }

    @Override
    public double getSum() {
        if (productOrder == null) return 0;
        return Order.getTotalPrice();
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
    }



    public static void addExtent(Invoice newInvoice) {
        if (newInvoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }

        for (Invoice existingInvoice : extent) {
            boolean sameName = existingInvoice.name.equals(newInvoice.name);

            boolean sameTIN = existingInvoice.taxIdentificationNumber == newInvoice.taxIdentificationNumber;

            boolean sameAddress = existingInvoice.address.equals(newInvoice.address);

            boolean sameProductOrder = existingInvoice.productOrder.equals(newInvoice.productOrder);

            boolean sameMethod = existingInvoice.getMethod() == newInvoice.getMethod();

            if (sameName && sameTIN && sameAddress && sameProductOrder && sameMethod) {
                throw new IllegalArgumentException("This Invoice already exists in extent");
            }
        }

        extent.add(newInvoice);
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
}