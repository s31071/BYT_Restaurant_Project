package classes;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Invoice extends Payment implements Serializable {
    public long ID;
    public long taxIdentificationNumber;
    public String name;
    private Address address;

    private List<SupplyHistory> supplyHistoryList = new ArrayList<>();

    private ProductOrder productOrder;

    public Invoice(PaymentMethod method, long ID, long taxIdentificationNumber, String name, Address address, ProductOrder productOrder) {
        super(method, 0.0);
        setID(ID);
        setTaxIdentificationNumber(taxIdentificationNumber);
        setName(name);
        setAddress(address);
        setProductOrder(productOrder);
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

    public void addSupplyHistory(SupplyHistory supplyHistory) {
        if (supplyHistory == null) return;
        supplyHistoryList.add(supplyHistory);
    }

    public List<SupplyHistory> getSupplyHistoryList() {
        return new ArrayList<>(supplyHistoryList);
    }
}

