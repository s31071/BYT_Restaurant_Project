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

    public Cook(String name, String surname, String phoneNumber, Address address, String email, LocalDate employmentDate, Contract contract, double yearsOfExperience, String title, String specialization) {
        super(name, surname, phoneNumber, address, email, employmentDate, contract);
        setYearsOfExperience(yearsOfExperience);
        setTitle(title);
        setSpecialization(specialization);
        addExtent(this);
    }

    @Override
    public double calculateSalary() {
        double base = getBaseSalary() * 168;

        double experienceBonus = base * (0.03 * yearsOfExperience);
        double employmentBonus = base * (0.02 * getYearsWorked());
        double specializationBonus = switch (specialization.toLowerCase()) {
            case "italian" -> 300;
            case "japanese" -> 400;
            case "french" -> 500;
            case "polish" -> 350;
            case "turkish" -> 250;
            default -> 200;
        };

        double contractFactor = contractMultiplier(getContract());

        return (base + experienceBonus + employmentBonus + specializationBonus)
                * contractFactor;
    }

    public void setYearsOfExperience(double yearsOfExperience) {
        if(yearsOfExperience < 0){
            throw new IllegalArgumentException("Years of experience cannot be negative");
        }
        this.yearsOfExperience = yearsOfExperience;
    }

    public void setTitle(String title) {
        if(title == null || title.isBlank()){
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
    }

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
        extent.add(cook);
    }

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