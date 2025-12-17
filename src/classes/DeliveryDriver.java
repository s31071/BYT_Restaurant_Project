package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;

public class DeliveryDriver implements IDeliveryDriver, Serializable {
    private static List<DeliveryDriver> extent = new ArrayList<>();
    private Employee employee;
    private String carModel;
    private String registrationNumber;
    private static final double kmBonus = 1;
    private boolean bonusApply;
    private double kmsInDay;
    private double kmsInMonth;
    public List<Order> orders;

    public DeliveryDriver() {}

    DeliveryDriver(Employee employee, String carModel, String registrationNumber, boolean bonusApply) throws Exception {
        if (employee == null) {
            throw new Exception("DeliveryDriver cannot exist without an Employee");
        }
        this.employee = employee;
        setCarModel(carModel);
        setRegistrationNumber(registrationNumber);
        setBonusApply(bonusApply);
        kmsInDay = 0;
        kmsInMonth = 0;
        orders = new ArrayList<>();
        addExtent(this);
    }

    @Override
    public void changeToCook(double yearsOfExperience, String title, String specialization) throws Exception {
        if (employee.getCook() != null) {
            throw new IllegalStateException("Employee is already a Cook");
        }
        clearOrders();
        removeFromExtent(this);
        employee.setDeliveryDriver(null);
        employee.setCook(new Cook(employee, yearsOfExperience, title, specialization));
    }

    @Override
    public void changeToWaiter(WorkwearSize workwearSize, double maximumTables) throws Exception {
        if (employee.getWaiter() != null) {
            throw new IllegalStateException("Employee is already a Waiter");
        }
        clearOrders();
        removeFromExtent(this);
        employee.setDeliveryDriver(null);
        employee.setWaiter(new Waiter(employee, workwearSize, maximumTables));
    }


    private void clearOrders() throws Exception {
        for (Order order : new ArrayList<>(orders)) {
            removeOrder(order);
        }
    }

    @Override
    public double calculateSalary() {
        double base = employee.getBaseSalary() * 168;
        double kmPay = isBonusApply() ? kmsInMonth * kmBonus : 0;
        double contractFactor = employee.contractMultiplier(employee.getContract());
        return (base + kmPay) * contractFactor;
    }

    public void confirmDelivery(Order order){
        order.setStatus(OrderStatus.DELIVERED);
    }

    public void saveDailyKms(){
        kmsInMonth += kmsInDay;
        kmsInDay = 0;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public String getCarModel() {
        return carModel;
    }

    @Override
    public void setCarModel(String carModel) {
        if(carModel == null || carModel.isBlank()){
            throw new IllegalArgumentException("Car model cannot be empty");
        }
        this.carModel = carModel;
    }

    @Override
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @Override
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

    @Override
    public boolean isBonusApply() {
        return bonusApply;
    }

    @Override
    public void setBonusApply(boolean bonusApply) {
        this.bonusApply = bonusApply;
    }

    @Override
    public double getKmsInDay() {
        return kmsInDay;
    }

    @Override
    public void setKmsInDay(double kmsInDay) {
        if(kmsInDay < 0){
            throw new IllegalArgumentException("Kms driven in a day cannot be negative");
        }
        this.kmsInDay = kmsInDay;
    }

    @Override
    public double getKmsInMonth() {
        return kmsInMonth;
    }

    @Override
    public void setKmsInMonth(double kmsInMonth) {
        if(kmsInMonth < 0){
            throw new IllegalArgumentException("Kms driven in a month cannot be negative");
        }
        this.kmsInMonth = kmsInMonth;
    }


    public void addOrder(Order order) throws Exception {
        if (order == null) {
            throw new Exception("Order cannot be empty");
        }
        orders.add(order);
        order.setDeliveryDriver(this);
    }

    public void removeOrder(Order order) throws Exception {
        if (order == null) {
            throw new Exception("Order cannot be empty");
        }
        orders.remove(order);
        order.setDeliveryDriver(null);
    }

    public static void addExtent(DeliveryDriver deliveryDriver) {
        if (deliveryDriver == null) {
            throw new IllegalArgumentException("Delivery driver cannot be null");
        }
        if (extent.contains(deliveryDriver)) {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryDriver that = (DeliveryDriver) o;
        return employee != null && employee.equals(that.employee);
    }

    @Override
    public int hashCode() {
        return employee != null ? employee.hashCode() : 0;
    }
}