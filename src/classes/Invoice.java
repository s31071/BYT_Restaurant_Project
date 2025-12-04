package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import java.util.Objects;

public class Invoice extends Payment implements Serializable {

    private static List<Invoice> extent = new ArrayList<>();

    private long ID;
    private long taxIdentificationNumber;
    private String name;
    private Address address;

    private List<SupplyHistory> supplyHistoryList = new ArrayList<>();

    private double sum;

    public Invoice() {}

    public Invoice(PaymentMethod method, long ID, long taxId, String name,
                   String street, String city, String postalCode, String country) {

        super(method);

        setID(ID);
        setTaxIdentificationNumber(taxId);
        setName(name);

        Address addr = new Address(street, city, postalCode, country);
        setAddress(addr);

        setSum();

        addExtent(this);
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

    public List<SupplyHistory> getSupplyHistoryList() {
        return Collections.unmodifiableList(supplyHistoryList);
    }

    public void addSupplyHistory(SupplyHistory sh) {
        if (sh == null) {
            throw new IllegalArgumentException("SupplyHistory cannot be null");
        }
        if (supplyHistoryList.contains(sh)) {
            throw new IllegalArgumentException("SupplyHistory already added");
        }

        supplyHistoryList.add(sh);

        this.sum += sh.getProductOrder().getTotalSum();
    }

    public void removeSupplyHistory(SupplyHistory sh) {
        throw new IllegalStateException("SupplyHistory cannot be removed from Invoice. Delete the SupplyHistory instance instead.");
    }

    @Override
    public void setSum() {
        double total = 0;

        for (SupplyHistory sh : supplyHistoryList) {
            if (sh.getProductOrder() != null) {
                double v = sh.getProductOrder().getTotalSum();
                if (v < 0) throw new IllegalStateException("ProductOrder total cannot be negative");
                total += v;
            }
        }

        this.sum = total;
    }

    @Override
    public double getSum() {
        return sum;
    }

    public static void addExtent(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        if (extent.contains(invoice)) {
            throw new IllegalArgumentException("Invoice already exists in system");
        }
        extent.add(invoice);
    }

    public static void removeFromExtent(Invoice invoice) {
        extent.remove(invoice);
    }

    public static List<Invoice> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void writeExtent(XMLEncoder out) throws IOException {
        out.writeObject(extent);
    }

    public static void readExtent(XMLDecoder in) throws IOException, ClassNotFoundException {
        extent = (List<Invoice>) in.readObject();
    }

    public static void clearExtent() {
        extent.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Invoice invoice = (Invoice) o;
        return ID == invoice.ID && taxIdentificationNumber == invoice.taxIdentificationNumber && Double.compare(sum, invoice.sum) == 0 && Objects.equals(name, invoice.name) && Objects.equals(address, invoice.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ID, taxIdentificationNumber, name, address, sum);
    }
}