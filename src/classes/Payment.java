package classes;

import java.io.Serializable;
import java.util.Objects;

public abstract class Payment implements Serializable {

    public abstract void setSum();
    public abstract double getSum();

    private PaymentMethod method;

    public Payment(){}
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return method == payment.method;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(method);
    }
}