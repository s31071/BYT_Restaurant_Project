package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;

public class DeliveryDriver extends Employee implements Serializable {
    private static List<DeliveryDriver> extent = new ArrayList<>();

    private String carModel;
    private String registrationNumber;
    private static final double kmBonus = 1;
    private boolean bonusApply;
    private double kmsInDay;
    private double kmsInMonth;
    private List<Order> orders;

    public DeliveryDriver(){}
    public DeliveryDriver(String name, String surname, String phoneNumber, String street, String city, String postalCode, String country, String email, LocalDate employmentDate, Contract contract, String carModel, String registrationNumber, boolean bonusApply, Employee manager) {
        super(name, surname, phoneNumber, street, city, postalCode, country, email, employmentDate, contract, manager);
        setCarModel(carModel);
        setRegistrationNumber(registrationNumber);
        setBonusApply(bonusApply);
        kmsInDay = 0;
        kmsInMonth = 0;
        addExtent(this);
        orders = new ArrayList<>();
    }

    @Override
    public double calculateSalary() {
        double base = getBaseSalary() * 168;

        double kmPay = isBonusApply() ? kmsInMonth * kmBonus : 0;

        double contractFactor = contractMultiplier(getContract());

        return (base + kmPay) * contractFactor;
    }

    private void confirmDelivery(Order order){
        order.setStatus(OrderStatus.DELIVERED);
    }

    private void saveDailyKms(){
        kmsInMonth += kmsInDay;
        kmsInDay = 0;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        if(carModel == null || carModel.isBlank()){
            throw new IllegalArgumentException("Car model cannot be empty");
        }
        this.carModel = carModel;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        String polishPlateRegex = "^[A-Z]{1,3}-?[A-Z0-9]{1,5}$";

        if (registrationNumber == null || registrationNumber.isBlank()) {
            throw new IllegalArgumentException("Registration number cannot be empty");
        }

        String normalized = registrationNumber.toUpperCase().replace(" ", "");

        if (!normalized.matches(polishPlateRegex)) {
            throw new IllegalArgumentException("Invalid Polish registration number format");
        }
        this.registrationNumber = registrationNumber;
    }

    public boolean isBonusApply() {
        return bonusApply;
    }

    public void setBonusApply(boolean bonusApply) {
        this.bonusApply = bonusApply;
    }

    public double getKmsInDay() {
        return kmsInDay;
    }

    public void setKmsInDay(double kmsInDay) {
        if(kmsInDay < 0){
            throw new IllegalArgumentException("Kms driven in a day cannot be negative");
        }
        this.kmsInDay = kmsInDay;
    }

    public double getKmsInMonth() {
        return kmsInMonth;
    }

    public void setKmsInMonth(double kmsInMonth) {
        if(kmsInMonth < 0){
            throw new IllegalArgumentException("Kms driven in a month cannot be negative");
        }
        this.kmsInMonth = kmsInMonth;
    }

    public static void addExtent(DeliveryDriver deliveryDriver) {
        if (deliveryDriver == null) {
            throw new IllegalArgumentException("Delivery driver cannot be null");
        }
        if(extent.contains(deliveryDriver)){
            throw new IllegalArgumentException("Such delivery driver is already in data base");
        }
        extent.add(deliveryDriver);
    }

    public static List<DeliveryDriver> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(DeliveryDriver deliveryDriver) {
        extent.remove(deliveryDriver);
    }

    public static void writeExtent(XMLEncoder out) throws IOException {
        out.writeObject(extent);
    }

    public static void readExtent(XMLDecoder in) throws IOException, ClassNotFoundException {
        extent = (List<DeliveryDriver>) in.readObject();
    }

    public static void clearExtent(){
        extent.clear();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}