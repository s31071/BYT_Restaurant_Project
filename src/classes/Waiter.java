package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;

public class Waiter implements IWaiter, Serializable {
    private static List<Waiter> extent = new ArrayList<>();
    private Employee employee;
    private WorkwearSize workwearSize;
    private double maximumTables;
    private HashSet<Reservation> reservations;

    public Waiter() {}

    Waiter(Employee employee, WorkwearSize workwearSize, double maximumTables) throws Exception {
        if (employee == null) {
            throw new Exception("Waiter cannot exist without an Employee");
        }
        this.employee = employee;
        setWorkwearSize(workwearSize);
        setMaximumTables(maximumTables);
        reservations = new HashSet<>();
        addExtent(this);
    }

    @Override
    public void changeToCook(double yearsOfExperience, String title, String specialization) throws Exception {
        if (employee.getCook() != null) {
            throw new IllegalStateException("Employee is already a Cook");
        }
        clearReservations();
        removeFromExtent(this);
        employee.setWaiter(null);
        employee.setCook(new Cook(employee, yearsOfExperience, title, specialization));
    }

    @Override
    public void changeToDeliveryDriver(String carModel, String registrationNumber, boolean bonusApply) throws Exception {
        if (employee.getDeliveryDriver() != null) {
            throw new IllegalStateException("Employee is already a DeliveryDriver");
        }
        clearReservations();
        removeFromExtent(this);
        employee.setWaiter(null);
        employee.setDeliveryDriver(new DeliveryDriver(employee, carModel, registrationNumber, bonusApply));
    }

    private void clearReservations() throws Exception {
        for (Reservation reservation : new HashSet<>(reservations)) {
            removeManagedReservation(reservation);
        }
    }

    @Override
    public double calculateSalary() {
        double base = employee.getBaseSalary() * 168;
        double yearsBonus = base * (0.01 * employee.getYearsWorked());
        double tableBonus = getMaximumTables() * 20;
        double contractFactor = employee.contractMultiplier(employee.getContract());
        return (base + yearsBonus + tableBonus) * contractFactor;
    }

    public void addManagedReservation(Reservation reservation) throws Exception {
        if(reservation == null){
            throw new Exception("Reservation cannot be null");
        }
        if(!reservations.contains(reservation)){
            reservations.add(reservation);
            if(reservation.getWaiterAssigned() != this) {
                reservation.addWaiterManaging(this);
            }
        }
    }

    public void removeManagedReservation(Reservation reservation) throws Exception {
        if (reservation == null) {
            throw new Exception("Reservation cannot be null");
        }

        if (reservations.remove(reservation)) {
            if (reservation.getWaiterAssigned() == this) {
                reservation.removeWaiterManaging(this);
            }
        }
    }

    public HashSet<Reservation> getReservations() {
        return reservations;
    }

    public Employee getEmployee() {
        return employee;
    }
    @Override
    public WorkwearSize getWorkwearSize() {
        return workwearSize;
    }

    @Override
    public void setWorkwearSize(WorkwearSize workwearSize) {
        if (workwearSize == null) {
            throw new IllegalArgumentException("Work wear size cannot be null");
        }
        this.workwearSize = workwearSize;
    }

    @Override
    public double getMaximumTables() {
        return maximumTables;
    }

    @Override
    public void setMaximumTables(double maximumTables) {
        if (maximumTables <= 0) {
            throw new IllegalArgumentException("Maximum tables must be greater than 0");
        }
        if (maximumTables >= 30) {
            throw new IllegalArgumentException("The waiter cannot be assigned to more than 30 tables");
        }
        this.maximumTables = maximumTables;
    }

    public static void addExtent(Waiter waiter) {
        if (waiter == null) {
            throw new IllegalArgumentException("Waiter cannot be null");
        }
        if (extent.contains(waiter)) {
            throw new IllegalArgumentException("Such waiter is already in data base");
        }
        extent.add(waiter);
    }

    public static List<Waiter> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Waiter waiter) {
        extent.remove(waiter);
    }

    public static void writeExtent(XMLEncoder out) throws IOException {
        out.writeObject(extent);
    }

    public static void readExtent(XMLDecoder in) throws IOException, ClassNotFoundException {
        extent = (List<Waiter>) in.readObject();
    }

    public static void clearExtent() {
        extent.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Waiter waiter = (Waiter) o;
        return employee != null && employee.equals(waiter.employee);
    }

    @Override
    public int hashCode() {
        return employee != null ? employee.hashCode() : 0;
    }
}
