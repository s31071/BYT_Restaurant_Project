package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ProductOrder implements Serializable {

    private static List<ProductOrder> extent = new ArrayList<>();

    // association ProductOrder - Supplier (1 , *)
    // many-to-many association ProductOrder - Product (* , *)
    // association ProductOrder - SupplyHistory (1 , *)
    private Supplier supplier;
    private List<Product> products = new ArrayList<>();
    private List<SupplyHistory> supplyHistoryList = new ArrayList<>();

    private double totalWeight;
    private double totalSum;

    public ProductOrder() {}

    public ProductOrder(List<Product> products, Supplier supplier) throws Exception {
        setProducts(products);
        addSupplier(supplier);
        setTotalWeight();
        setTotalSum();
        addExtent(this);

        for (Product p : products) {
            if (!p.getProductOrders().contains(this)) {
                p.addProductOrder(this);
            }
        }
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void addSupplier(Supplier supplier) throws Exception {

        if(this.supplier == null) {
            setSupplier(supplier);

            if(!supplier.getProductOrders().contains(this)){
                supplier.addOrderedProduct(this);
            }
        }else if (this.supplier != supplier) {
            throw new Exception("This product order already has a supplier assigned");
        }
    }

    public void removeSupplier(Supplier supplier) throws Exception {
        if (supplier == null) {
            throw new Exception("Supplier cannot be null");
        }

        if (this.supplier != supplier) {
            return;
        }

        this.supplier = null;

        if (supplier.getProductOrders().contains(this)) {
            supplier.removeOrderedProduct(this);
        }
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void setProducts(List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Product list cannot be empty");
        }

        for (Product p : products) {
            if (p == null) {
                throw new IllegalArgumentException("Product cannot be null");
            }
        }

        this.products = new ArrayList<>(products);
        setTotalWeight();
        setTotalSum();
    }

    public void addProduct(Product p) {
        if (p == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (products.contains(p)) {
            return;
        }

        products.add(p);

        if (!p.getProductOrders().contains(this)) {
            p.addProductOrder(this);
        }

        setTotalWeight();
        setTotalSum();
    }

    public void removeProduct(Product p) {
        if (p == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (!products.contains(p)) {
            return;
        }

        if (products.size() == 1) {
            throw new IllegalStateException("ProductOrder must contain at least one Product");
        }

        products.remove(p);

        if (p.getProductOrders().contains(this)) {
            p.removeProductOrder(this);
        }

        setTotalWeight();
        setTotalSum();
    }

    public List<SupplyHistory> getSupplyHistoryList() {
        return Collections.unmodifiableList(supplyHistoryList);
    }

    public void addSupplyHistory(SupplyHistory sh) {
        if (sh == null) {
            throw new IllegalArgumentException("SupplyHistory cannot be null");
        }

        supplyHistoryList.add(sh);

        if (sh.getProductOrder() != this) {
            sh.setProductOrder(this);
        }
    }

    public void removeSupplyHistory(SupplyHistory sh) {
        throw new IllegalStateException("SupplyHistory cannot be detached from ProductOrder. Delete the SupplyHistory instance instead.");
    }

    public void setSupplier(Supplier supplier) throws Exception {
        if(supplier == null){
            throw new Exception("Supplier cannot be null");
        }
        this.supplier = supplier;
    }

    private void setTotalSum() {
        this.totalSum = Math.round(
                products.stream()
                        .mapToDouble(Product::getPrice)
                        .sum() * 100.0
        ) / 100.0;
    }

    public double getTotalSum() {
        return totalSum;
    }

    private void setTotalWeight() {
        this.totalWeight = Math.round(
                products.stream()
                        .mapToDouble(Product::getWeight)
                        .sum() * 100.0
        ) / 100.0;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public static void addExtent(ProductOrder po) {
        if (po == null) {
            throw new IllegalArgumentException("ProductOrder cannot be null");
        }
        if (extent.contains(po)) {
            throw new IllegalArgumentException("ProductOrder already exists");
        }
        extent.add(po);
    }

    public static List<ProductOrder> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(ProductOrder po) {
        extent.remove(po);
    }

    public static void writeExtent(XMLEncoder out) throws java.io.IOException {
        out.writeObject(extent);
    }

    public static void readExtent(XMLDecoder in) throws java.io.IOException, ClassNotFoundException {
        extent = (List<ProductOrder>) in.readObject();
    }

    public static void clearExtent() {
        extent.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductOrder that = (ProductOrder) o;
        return Double.compare(totalWeight, that.totalWeight) == 0 && Double.compare(totalSum, that.totalSum) == 0 && Objects.equals(supplier, that.supplier) && Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supplier, totalWeight, totalSum);
    }
}