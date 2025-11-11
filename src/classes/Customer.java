package classes;

import java.util.ArrayList;
import java.util.List;

public class Customer extends Person{
    private static List<Customer> customerList = new ArrayList<>();

    private double loyaltyPoints;
    public Customer(String name, String surname, String phoneNumber, String address, String email) {
        super(name, surname, phoneNumber, address, email);
        loyaltyPoints = 0;
        addCustomer(this);
    }

    private void updateLoyaltyPoints(double newPoints){
        loyaltyPoints+=newPoints;
    }

    private static void addCustomer(Customer customer){
        if(customer == null){
            throw new IllegalArgumentException("Customer cannot be null");
        }

        customerList.add(customer);
    }

    public void displayCustomerInfo(){
        System.out.println("Name: "+this.getName());
        System.out.println("Surname: "+this.getSurname());
        System.out.println("Phone number: "+this.getPhoneNumber());
        System.out.println("Address: "+this.getAddress());
        System.out.println("Email: "+this.getEmail());
        System.out.println("Loyalty points: "+this.loyaltyPoints);
    }
}
