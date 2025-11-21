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

    public static final double service = 0.1;
    public Double tip;

    private Order order;
    private double sum;

    public Receipt(PaymentMethod method, Order order, Double tip) {
        super(method);
        setOrder(order);
        setTip(tip);
        setSum();
        addExtent(this);
    }

    public Receipt(){}
    public Receipt(PaymentMethod method, Order order) {
        this(method, order, null);
    }

    public Double getTip() {
        return tip;
    }

    public void setTip(Double tip) {
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

    public static void addExtent(Receipt receipt) {
        if(receipt == null){
            throw new IllegalArgumentException("Receipt cannot be null");
        }
        if(extent.contains(receipt)){
            throw new IllegalArgumentException("Such receipt is already in data base");
        }
        extent.add(receipt);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
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