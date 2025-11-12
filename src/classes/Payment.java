package classes;

import java.io.Serializable;

public abstract class Payment implements Serializable {

    //jest jako public, żeby mieć do niego dostęp z innych klas jak potrzeba
    public enum PaymentMethod {
        CARD,
        CASH,
        VOUCHER
    }
    //derived attributes nie mają settera, tylko get
    public double sum; //derived attribute


    private PaymentMethod method;
    public Payment(PaymentMethod method, double sum) {
        setPaymentMethod(method);
        this.sum = sum;
    }
    public void setPaymentMethod(PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
        this.method = method;
    }


    public double getSum() {
        return sum;
    }
    public PaymentMethod getPaymentMethod() {
        return method;
    }

}

