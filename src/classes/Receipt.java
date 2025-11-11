package classes;

import java.io.Serializable;

public class Receipt extends Payment implements Serializable {
    public static final double service = 0.1; //cosntant value
    //musi być z dużej litery, żeby było nullable
    public Double tip; //nullable

    private Order order;

    public Receipt(PaymentMethod method, Order order, Double tip) {
        super(method, 0); //narazie 0, żeby mogło sie policzyć z tipem
        setOrder(order);
        setTip(tip);
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
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        this.order = order;
    }

    //funkcja do wliczenia tipa i serwisu do naszej kwoty zamówienia
    public double getFinalAmount() {
        double base = order.getTotalPrice();
        double total = base + (base * service);
        if (tip != null) total += tip;
        return total;
    }
}

