package classes;

import java.util.ArrayList;
import java.util.List;

public class Supplier extends Person{
    private static List<Supplier> supplierList = new ArrayList<>();

    private String companyName;
    private Category category;
    private double deliveryCost;

    public Supplier(String name, String surname, String phoneNumber, String address, String email, String companyName, Category category, double deliveryCost) {
        super(name, surname, phoneNumber, address, email);
        this.companyName = validateCompanyName(companyName);
        this.category = category;
        this.deliveryCost = deliveryCost;

        addSupplier(this);
    }

    private String validateCompanyName(String companyName){
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }
        return companyName;
    }

    private static void addSupplier(Supplier supplier){
        if(supplier == null){
            throw new IllegalArgumentException("Supplier cannot be null");
        }

        supplierList.add(supplier);
    }

    public static List<Supplier> getSupplierList() {
        return supplierList;
    }

    public static void setSupplierList(List<Supplier> supplierList) {
        Supplier.supplierList = supplierList;
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
}

