package classes;

import java.time.LocalDate;

public class Cook extends Employee{
    private double yearsOfExperience;
    private String title;
    private String specialization;

    public Cook(String name, String surname, String phoneNumber, String address, String email, LocalDate employmentDate, Contract contract, double yearsOfExperience, String title, String specialization) {
        super(name, surname, phoneNumber, address, email, employmentDate, contract);
        this.yearsOfExperience = yearsOfExperience;
        this.title = title;
        this.specialization = specialization;
    }


    @Override
    double calculateSalary(Contract contract, LocalDate employmentDate) {
        return 0;
    }
}
