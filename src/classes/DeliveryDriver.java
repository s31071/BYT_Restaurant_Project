package classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class DeliveryDriver extends Employee implements Serializable {
    private static List<DeliveryDriver> extent = new ArrayList<>();

    private String carModel;
    private String registrationNumber;
    private static final double kmBonus = 1;
    private boolean bonusApply;
    private double kmsInDay;
    private double kmsInMonth;

    public DeliveryDriver(String name, String surname, String phoneNumber, String address, String email, LocalDate employmentDate, Contract contract, String carModel, String registrationNumber, boolean bonusApply) {
        super(name, surname, phoneNumber, address, email, employmentDate, contract);
        this.carModel = carModel;
        this.registrationNumber = registrationNumber;
        this.bonusApply = bonusApply;
        kmsInDay = 0;
        kmsInMonth = 0;
        addExtent(this);
    }

    @Override
    public double calculateSalary(Contract contract, LocalDate employmentDate) {
        return getBaseSalary() * 168 + kmsInMonth * (bonusApply ? kmBonus : 0);
    }

    private void confirmDelivery(){}

    private void saveDailyKms(){
        kmsInMonth += kmsInDay;
        kmsInDay = 0;
    }

    private void reportIssue(){}


    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
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
        this.kmsInDay = kmsInDay;
    }

    public double getKmsInMonth() {
        return kmsInMonth;
    }

    public void setKmsInMonth(double kmsInMonth) {
        this.kmsInMonth = kmsInMonth;
    }

    public static void addExtent(DeliveryDriver deliveryDriver) {
        if (deliveryDriver == null) {
            throw new IllegalArgumentException("Delivery driver cannot be null");
        }
        extent.add(deliveryDriver);
    }

    public static List<DeliveryDriver> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(DeliveryDriver deliveryDriver) {
        extent.remove(deliveryDriver);
    }

    public static void writeExtent(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<DeliveryDriver>) objectInputStream.readObject();
    }
}