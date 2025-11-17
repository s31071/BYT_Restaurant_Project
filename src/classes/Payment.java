package classes;

import java.io.Serializable;


public abstract class Payment implements Serializable {

    //derived attributes nie mają settera, tylko get
    public abstract double getSum(); //derived attribute

    private PaymentMethod method;

    public Payment(PaymentMethod method) {
        this.method = method;
    }
}