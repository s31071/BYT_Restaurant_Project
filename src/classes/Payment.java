package classes;

import java.io.Serializable;

public abstract class Payment implements Serializable {

    public abstract void setSum();   // derived attribute setter
    public abstract double getSum(); // derived attribute getter

    private PaymentMethod method;

    public Payment(PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
        this.method = method;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
        this.method = method;
    }
}