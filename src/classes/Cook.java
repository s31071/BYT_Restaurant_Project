package classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cook extends Employee{
    private static List<Cook> cookList = new ArrayList<>();

    private double yearsOfExperience;
    private String title;
    private String specialization;

    public Cook(String name, String surname, String phoneNumber, String address, String email, LocalDate employmentDate, Contract contract, double yearsOfExperience, String title, String specialization) {
        super(name, surname, phoneNumber, address, email, employmentDate, contract);
        this.yearsOfExperience = yearsOfExperience;
        this.title = title;
        this.specialization = specialization;

        addCook(this);
    }


    @Override
    public double calculateSalary(Contract contract, LocalDate employmentDate) {
        return 0;
    }

    private static void addCook(Cook cook){
        if(cook == null){
            throw new IllegalArgumentException("Cook cannot be null");
        }

        cookList.add(cook);
    }
}
