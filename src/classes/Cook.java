package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;

public class Cook implements ICook, Serializable {
    private static List<Cook> extent = new ArrayList<>();
    private Employee employee;
    private double yearsOfExperience;
    private String title;
    private String specialization;

    public Cook() {}

    Cook(Employee employee, double yearsOfExperience, String title, String specialization) throws Exception {
        if (employee == null) {
            throw new Exception("Cook cannot exist without an Employee");
        }
        this.employee = employee;
        setYearsOfExperience(yearsOfExperience);
        setTitle(title);
        setSpecialization(specialization);
        addExtent(this);
    }

    @Override
    public void changeToWaiter(WorkwearSize workwearSize, double maximumTables) throws Exception {
        if (employee.getWaiter() != null) {
            throw new IllegalStateException("Employee is already a Waiter");
        }
        removeFromExtent(this);
        employee.setCook(null);
        employee.setWaiter(new Waiter(employee, workwearSize, maximumTables));
    }

    @Override
    public void changeToDeliveryDriver(String carModel, String registrationNumber, boolean bonusApply) throws Exception {
        if (employee.getDeliveryDriver() != null) {
            throw new IllegalStateException("Employee is already a DeliveryDriver");
        }
        removeFromExtent(this);
        employee.setCook(null);
        employee.setDeliveryDriver(new DeliveryDriver(employee, carModel, registrationNumber, bonusApply));
    }

    @Override
    public double calculateSalary() {
        double base = employee.getBaseSalary() * 168;
        double experienceBonus = base * (0.03 * yearsOfExperience);
        double employmentBonus = base * (0.02 * employee.getYearsWorked());
        double specializationBonus = switch (specialization.toLowerCase()) {
            case "italian" -> 300;
            case "japanese" -> 400;
            case "french" -> 500;
            case "polish" -> 350;
            case "turkish" -> 250;
            default -> 200;
        };
        double contractFactor = employee.contractMultiplier(employee.getContract());
        return (base + experienceBonus + employmentBonus + specializationBonus) * contractFactor;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public double getYearsOfExperience() {
        return yearsOfExperience;
    }

    @Override
    public void setYearsOfExperience(double yearsOfExperience) {
        if(yearsOfExperience < 0){
            throw new IllegalArgumentException("Years of experience cannot be negative");
        }
        this.yearsOfExperience = yearsOfExperience;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        if(title == null || title.isBlank()){
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
    }

    @Override
    public String getSpecialization() {
        return specialization;
    }

    @Override
    public void setSpecialization(String specialization) {
        if(specialization == null || specialization.isBlank()){
            throw new IllegalArgumentException("Specialization cannot be empty");
        }
        this.specialization = specialization;
    }
    public static void addExtent(Cook cook) {
        if(cook == null){
            throw new IllegalArgumentException("Cook cannot be null");
        }
        if(extent.contains(cook)){
            throw new IllegalArgumentException("Such cook is already in data base");
        }
        extent.add(cook);
    }

    public static List<Cook> getExtent() {
        return Collections.unmodifiableList(extent);
    }
    public static void removeFromExtent(Cook cook) {
        extent.remove(cook);
    }
    public static void clearExtent() {
        extent.clear();
    }
    public static void writeExtent(XMLEncoder out) throws IOException {
        out.writeObject(extent);
    }
    public static void readExtent(XMLDecoder in) throws IOException, ClassNotFoundException {
        extent = (List<Cook>) in.readObject();
    }
     @Override
     public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;
         Cook cook = (Cook) o;
         return employee != null && employee.equals(cook.employee);
     }

     @Override
     public int hashCode() {
         return employee != null ? employee.hashCode() : 0;
     }
}
