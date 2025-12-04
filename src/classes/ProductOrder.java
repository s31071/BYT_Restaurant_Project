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
        setSupplier(supplier);
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

    public void setSupplier(Supplier newSupplier) throws Exception {

        if (newSupplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null for ProductOrder (multiplicity 1)");
        }

        if (this.supplier == newSupplier) {
            return;
        }

        if (this.supplier != null) {
            Supplier old = this.supplier;
            this.supplier = null;

            old.getProductOrders().remove(this);
        }

        this.supplier = newSupplier;

        if (!newSupplier.getProductOrders().contains(this)) {
            newSupplier.addOrderedProduct(this);
        }
    }

    public void removeSupplier() {
        throw new IllegalStateException("ProductOrder must always have exactly 1 Supplier. Removing Supplier is not allowed.");
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
        return Double.compare(getTotalWeight(), that.getTotalWeight()) == 0 && Double.compare(getTotalSum(), that.getTotalSum()) == 0 && Objects.equals(getSupplier(), that.getSupplier()) && Objects.equals(getProducts(), that.getProducts()) && Objects.equals(getSupplyHistoryList(), that.getSupplyHistoryList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSupplier(), getProducts(), getSupplyHistoryList(), getTotalWeight(), getTotalSum());
    }
}