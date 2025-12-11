package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.io.IOException;

public class SupplyHistory implements Serializable {

    private static List<SupplyHistory> extent = new ArrayList<>();

    private LocalDate date;
    private SupplyStatus status;
    private Invoice invoice;
    private ProductOrder productOrder;
    private UUID uuid;
    public SupplyHistory(LocalDate date, SupplyStatus status) {}
    // association SupplyHistory - Invoice (1 , *)
    // association SupplyHistory - ProductOrder (1 , *)
    public SupplyHistory() {}

    public SupplyHistory(LocalDate date, SupplyStatus status, Invoice invoice, ProductOrder productOrder) {
        uuid = UUID.randomUUID();
        setDate(date);
        setStatus(status);
        setInvoice(invoice);
        setProductOrder(productOrder);
        addExtent(this);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null)
            throw new IllegalArgumentException("Date cannot be null");
        if (date.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Date cannot be in the future");
        this.date = date;
    }

    public SupplyStatus getStatus() {
        return status;
    }

    public void setStatus(SupplyStatus newStatus) {
        if (newStatus == null)
            throw new IllegalArgumentException("Status cannot be null");

        if (newStatus == SupplyStatus.DELIVERED) {
            validateDeliveredState();
        }

        this.status = newStatus;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice newInvoice) {
        if (newInvoice == null)
            throw new IllegalArgumentException("Invoice cannot be null");

        if (this.invoice == newInvoice)
            return;

        this.invoice = newInvoice;

        if (!newInvoice.getSupplyHistorySet().contains(this)) {
            newInvoice.addSupplyHistory(this);
        }
    }

    public void removeInvoice() {
        throw new IllegalStateException("SupplyHistory must always have an Invoice. Removal is not allowed.");
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder newPO) {
        if (newPO == null)
            throw new IllegalArgumentException("ProductOrder cannot be null");

        if (this.productOrder == newPO)
            return;

        this.productOrder = newPO;

        if (!newPO.getSupplyHistories().contains(this)) {
            newPO.addSupplyHistory(this);
        }
    }

    public void removeProductOrder() {
        throw new IllegalStateException("SupplyHistory must always have a ProductOrder. Removal is not allowed.");
    }

    private void validateDeliveredState() {

        List<SupplyHistory> earlierOrdered = extent.stream()
                .filter(sh -> sh != this)
                .filter(sh -> sh.getInvoice().equals(this.invoice))
                .filter(sh -> sh.getProductOrder().equals(this.productOrder))
                .filter(sh -> sh.getStatus() == SupplyStatus.ORDERED)
                .filter(sh -> sh.getDate().isBefore(this.date))
                .toList();

        if (earlierOrdered.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot set DELIVERED: No earlier ORDERED exists for same invoice & product order.");
        }
    }

    public static void addExtent(SupplyHistory sh) {
        if (sh == null)
            throw new IllegalArgumentException("SupplyHistory cannot be null");
        if (extent.contains(sh))
            throw new IllegalArgumentException("This SupplyHistory already exists");
        extent.add(sh);
    }

    public static List<SupplyHistory> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(SupplyHistory sh) {
        extent.remove(sh);
    }

    public static void writeExtent(XMLEncoder out) throws IOException {
        out.writeObject(extent);
    }

    public static void readExtent(XMLDecoder in) throws IOException, ClassNotFoundException {
        extent = (List<SupplyHistory>) in.readObject();
    }

    public static void clearExtent() {
        extent.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SupplyHistory that = (SupplyHistory) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}