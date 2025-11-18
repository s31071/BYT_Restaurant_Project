package classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class PartTime extends Employee implements Serializable {
    private static List<PartTime> extent = new ArrayList<>();

    public Type type;
    public double salary;

    public PartTime(String name, String surname, String phoneNumber, Address address, String email, LocalDate employmentDate, Contract contract, Type type) {
        super(name, surname, phoneNumber, address, email, employmentDate, contract);
        setType(type);
        this.salary = calculateSalary();
        addExtent(this);
    }

    @Override
    public double calculateSalary() {
        return switch (type){
            case HALF_TIME -> 20 * 4.5 * getBaseSalary();
            case THREE_QUARTER_TIME -> 30 * 4.5 * getBaseSalary();
            case ON_CALL -> 10 * 4.5 * getBaseSalary();
        };
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        if(type == null){
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
    }

    @Override
    public double getSalary() {
        return salary;
    }

    public static void addExtent(PartTime partTime) {
        if (partTime == null) {
            throw new IllegalArgumentException("Part time cannot be null");
        }
        extent.add(partTime);
    }

    public static List<PartTime> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(PartTime partTime) {
        extent.remove(partTime);
    }

    public static void writeExtent(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<PartTime>) objectInputStream.readObject();
    }
}
