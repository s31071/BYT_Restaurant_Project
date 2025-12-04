package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;

public class Waiter extends Employee implements Serializable {
    private static List<Waiter> extent = new ArrayList<>();

    public WorkwearSize workwearSize;
    public double maximumTables;

    private HashSet<Reservation> reservations;

    public Waiter(){}

    public Waiter(String name, String surname, String phoneNumber, String street, String city, String postalCode, String country, String email, LocalDate employmentDate, Contract contract, WorkwearSize workwearSize, double maximumTables, Employee manager) {
        super(name, surname, phoneNumber, street, city, postalCode, country, email, employmentDate, contract, manager);
        setWorkwearSize(workwearSize);
        setMaximumTables(maximumTables);
        addExtent(this);

        reservations = new HashSet<>();
    }

    public void addManagedReservation(Reservation reservation) throws Exception {
        if(reservation == null){
            throw new Exception("Reservation cannot be null");
        }
        reservations.add(reservation);
        reservation.setWaiterAssigned(this);
    }

    public void removeManagedReservation(Reservation reservation) throws Exception {
        if(reservation == null){
            throw new Exception("Reservation cannot be null");
        }
        if(!reservations.contains(reservation)){
            throw new Exception("This reservation is not managed by this waiter");
        }
        reservations.remove(reservation);
        reservation.setWaiterAssigned(null);
    }

    @Override
    public double calculateSalary() {
        double base = getBaseSalary() * 168;
        double yearsBonus = base * (0.01 * getYearsWorked());
        double tableBonus = getMaximumTables() * 20;

        double contractFactor = contractMultiplier(getContract());

        return (base + yearsBonus + tableBonus) * contractFactor;
    }

    public HashSet<Reservation> getReservations() {
        return reservations;
    }

    public WorkwearSize getWorkwearSize() {
        return workwearSize;
    }

    public void setWorkwearSize(WorkwearSize workwearSize) {
        if(workwearSize == null){
            throw new IllegalArgumentException("Work wear size cannot be null");
        }
        this.workwearSize = workwearSize;
    }

    public double getMaximumTables() {
        return maximumTables;
    }

    public void setMaximumTables(double maximumTables) {
        if (maximumTables <= 0) {
            throw new IllegalArgumentException("Maximum tables must be greater than 0");
        }
        if (maximumTables >= 30){
            throw new IllegalArgumentException("The waiter cannot be assigned to more than 30 tables"); //TODO: zapisać to w dokumentacji
        }
        this.maximumTables = maximumTables;
    }

    public static void addExtent(Waiter waiter){
        if(waiter == null){
            throw new IllegalArgumentException("Waiter cannot be null");
        }
        if(extent.contains(waiter)){
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
