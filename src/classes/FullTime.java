package classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class FullTime extends Employee implements Serializable {
    private static List<FullTime> extent = new ArrayList<>();

    private static final double hoursPerWeek = 40;

    public FullTime(String name, String surname, String phoneNumber, String address, String email, LocalDate employmentDate, Contract contract) {
        super(name, surname, phoneNumber, address, email, employmentDate, contract);
        addExtent(this);
    }

    @Override
    public double calculateSalary(Contract contract, LocalDate employmentDate){
        double workingYears = LocalDate.now().getYear() - employmentDate.getYear() + 1;
        return switch (contract) {
            case EMPLOYMENT_CONTRACT -> 0.75 * workingYears * hoursPerWeek * 4.5 * getBaseSalary();
            case MANDATE_CONTRACT -> 0.9 * workingYears * hoursPerWeek * 4.5 * getBaseSalary();
            case B2B -> workingYears * hoursPerWeek * 4.5 * getBaseSalary();
        };
    }

    public static void addExtent(FullTime fullTime){
        if(fullTime == null){
            throw new IllegalArgumentException("Full time cannot be null");
        }
        extent.add(fullTime);
    }

    public static List<FullTime> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(FullTime fullTime) {
        extent.remove(fullTime);
    }

    public static void writeExtent(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<FullTime>) objectInputStream.readObject();
    }
}