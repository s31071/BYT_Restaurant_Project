package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import java.util.Objects;

public class Receipt extends Payment implements Serializable {

    private static List<Receipt> extent = new ArrayList<>();

    public static final double service = 0.1;

    private Double tip;
    // association Receipt - Order (1 , 1)
    private Order order;
    private double sum;

    public Receipt() {}

    public Receipt(PaymentMethod method, Order order, Double tip) {
        super(method);

        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null when creating a Receipt");
        }

        setTip(tip);
        setOrder(order);
        setSum();
        addExtent(this);
    }

    public Receipt(PaymentMethod method, Order order) {
        this(method, order, null);
    }

    public Double getTip() {
        return tip;
    }

    public void setTip(Double tip) {
        if (tip != null && tip < 0) {
            throw new IllegalArgumentException("Tip cannot be negative");
        }
        this.tip = tip;
        setSum();
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order newOrder) {
        if (newOrder == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        if (this.order == null) {
            this.order = newOrder;
            if (newOrder.getReceipt() != this) {
                newOrder.setReceipt(this);
            }
        } else if (this.order != newOrder) {
            throw new IllegalStateException("This receipt already has an assigned order");
        }

        setSum();
    }

    public void removeOrder() {
        throw new IllegalStateException("Receipt must always have exactly 1 Order.");
    }

    @Override
    public void setSum() {
        if (order == null) {
            this.sum = 0.0;
            return;
        }

        double base = order.getTotalPrice();
        if (base < 0) {
            throw new IllegalStateException("Order total price cannot be negative");
        }

        double total = base + (base * service);

        if (tip != null) {
            total += tip;
        }

        if (total < 0) {
            throw new IllegalStateException("Calculated sum cannot be negative");
        }

        this.sum = total;
    }

    @Override
    public double getSum() {
        return sum;
    }

    public static void addExtent(Receipt receipt) {
        if (receipt == null) {
            throw new IllegalArgumentException("Receipt cannot be null");
        }
        if (extent.contains(receipt)) {
            throw new IllegalArgumentException("This Receipt already exists in the system");
        }
        extent.add(receipt);
    }

    public static void removeFromExtent(Receipt receipt) {
        if (receipt == null) return;
        extent.remove(receipt);
    }

    public static List<Receipt> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void writeExtent(XMLEncoder out) throws IOException {
        out.writeObject(extent);
    }

    public static void readExtent(XMLDecoder in) throws IOException, ClassNotFoundException {
        extent = (List<Receipt>) in.readObject();
    }

    public static void clearExtent() {
        extent.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Receipt receipt = (Receipt) o;
        return Double.compare(sum, receipt.sum) == 0 && Objects.equals(tip, receipt.tip) && Objects.equals(order, receipt.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tip, order, sum);
    }
}