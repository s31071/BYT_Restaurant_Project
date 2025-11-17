package classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class Cook extends Employee implements Serializable {
    private static List<Cook> extent = new ArrayList<>();

    private double yearsOfExperience;
    private String title;
    private String specialization;

    public Cook(String name, String surname, String phoneNumber, String address, String email, LocalDate employmentDate, Contract contract, double yearsOfExperience, String title, String specialization) {
        super(name, surname, phoneNumber, address, email, employmentDate, contract);
        this.yearsOfExperience = yearsOfExperience;
        this.title = title;
        this.specialization = specialization;
        addExtent(this);
    }

    @Override
    public double calculateSalary(Contract contract, LocalDate employmentDate) {
        return 0;
    }

    public static void addExtent(Cook cook) {
        if(cook == null){
            throw new IllegalArgumentException("Cook cannot be null");
        }
        extent.add(cook);
    }

    @Override
    public List<Cook> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Cook cook) {
        extent.remove(cook);
    }

    public static void writeExtent(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Cook>) objectInputStream.readObject();
    }
}