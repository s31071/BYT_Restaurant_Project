package classes;

//association class with bag between invoice and productOrder
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class SupplyHistory implements Serializable {
    private static List<SupplyHistory> extent = new ArrayList<>();

    public LocalDate date;

    private Invoice invoice;
    private ProductOrder productOrder;
    private SupplyStatus status;

    public SupplyHistory(LocalDate date, SupplyStatus status, Invoice invoice, ProductOrder productOrder) {
        setDate(date);
        setInvoice(invoice);
        setProductOrder(productOrder);
        setStatus(status);
        addExtent(this);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be empty");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the future");
        }
        this.date = date;
    }

    public SupplyStatus getStatus() {
        return status;
    }

    public void setStatus(SupplyStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        checkStatuses(newStatus);
        this.status = newStatus;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    private void setInvoice(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be empty");
        }
        this.invoice = invoice;
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    private void setProductOrder(ProductOrder productOrder) {
        if (productOrder == null) {
            throw new IllegalArgumentException("Product order cannot be empty");
        }
        this.productOrder = productOrder;
    }

    private void checkStatuses(SupplyStatus newStatus) {

        if (newStatus == SupplyStatus.DELIVERED) {

            boolean hasOrderedBefore = extent.stream().anyMatch(
                    sh -> sh.getInvoice().equals(this.invoice)
                            && sh.getProductOrder().equals(this.productOrder)
                            && sh.getStatus() == SupplyStatus.ORDERED
                            && sh.getDate().isBefore(this.date)
            );

            if (!hasOrderedBefore) {
                throw new IllegalArgumentException(
                        "Cannot mark DELIVERED: no prior ORDERED entry exists."
                );
            }

            boolean wrongDateOrder = extent.stream().anyMatch(
                    sh -> sh.getInvoice().equals(this.invoice)
                            && sh.getProductOrder().equals(this.productOrder)
                            && sh.getStatus() == SupplyStatus.ORDERED
                            && !sh.getDate().isBefore(this.date)
            );

            if (wrongDateOrder) {
                throw new IllegalArgumentException(
                        "DELIVERED date must be after the ORDERED date."
                );
            }
        }
    }

    public static void addExtent(SupplyHistory newSupplyHistory) {
        if (newSupplyHistory == null) {
            throw new IllegalArgumentException("SupplyHistory cannot be null");
        }

        for (SupplyHistory existingSupplyHistory : extent) {
            boolean sameDate = existingSupplyHistory.date.equals(newSupplyHistory.date);
            boolean sameStatus = existingSupplyHistory.status == newSupplyHistory.status;
            boolean sameInvoice = existingSupplyHistory.invoice.equals(newSupplyHistory.invoice);
            boolean sameProductOrder = existingSupplyHistory.productOrder.equals(newSupplyHistory.productOrder);

            if (sameDate && sameStatus && sameInvoice && sameProductOrder) {
                throw new IllegalArgumentException("This SupplyHistory already exists in extent");
            }
        }

        extent.add(newSupplyHistory);
    }

    public static List<SupplyHistory> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(SupplyHistory supplyHistory) {
        extent.remove(supplyHistory);
    }

    public static void writeExtent(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<SupplyHistory>) objectInputStream.readObject();
    }
}