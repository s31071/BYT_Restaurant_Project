package classes;

//association class with bag between invoice and productOrder

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SupplyHistory implements Serializable {
    public LocalDate date;
    public enum Status {
        ORDERED,
        DELIVERED
    };
    private Invoice invoice;
    private ProductOrder productOrder;
    private Status status;

    //przez to, że to jest association class musi być zrobiony extent
    private static List<SupplyHistory> supplyHistoryList = new ArrayList<>();


    public SupplyHistory(LocalDate date, Status status, Invoice invoice, ProductOrder productOrder) {
        setDate(date);
        setStatus(status);
        setInvoice(invoice);
        setProductOrder(productOrder);

        invoice.addSupplyHistory(this);
        productOrder.addSupplyHistory(this);

        supplyHistoryList.add(this);
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        this.status = status;
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

    public static List<SupplyHistory> getSupplyHistoryList() {
        return new ArrayList<>(supplyHistoryList);
    }

}

