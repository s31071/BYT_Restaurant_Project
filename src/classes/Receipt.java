package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;

public class Receipt extends Payment implements Serializable {
    private static List<Receipt> extent = new ArrayList<>();

    public static final double service = 0.1; //cosntant value
    public Double tip; //nullable

    private Order order;
    private double sum;

    public Receipt(PaymentMethod method, Order order, Double tip) {
        super(method);
        setOrder(order);
        setTip(tip);
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
        if (tip != null) {
            if (tip < 0) {
                throw new IllegalArgumentException("Tip cannot be negative");
            }
        }
        this.tip = tip;
        setSum();
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        this.order = order;
        setSum();
    }
    @Override
    public void setSum() {
        double base = order.getTotalPrice();
        double total = base + (base * service);
        if (tip != null) total += tip;
        this.sum = total;
    }

    @Override
    public double getSum() {
        return sum;
    }

    public static void addExtent(Receipt newReceipt) {
        if (newReceipt == null) {
            throw new IllegalArgumentException("Receipt cannot be null");
        }

        for (Receipt existingReceipt : extent) {
            boolean sameOrder = existingReceipt.order.equals(newReceipt.order);

            boolean sameTip = (existingReceipt.tip == null && newReceipt.tip == null)
                    || (existingReceipt.tip != null && newReceipt.tip != null && existingReceipt.tip.equals(newReceipt.tip));

            boolean sameMethod = existingReceipt.getMethod() == newReceipt.getMethod();

            if (sameOrder && sameTip && sameMethod) {
                throw new IllegalArgumentException("This receipt already exists in extent");
            }
        }

        extent.add(newReceipt);
    }

    public static List<Receipt> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Receipt receipt) {
        extent.remove(receipt);
    }

    public static void writeExtent(XMLEncoder objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(XMLDecoder objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Receipt>) objectInputStream.readObject();
    }

    public static void clearExtent(){
        extent.clear();
    }
}