package classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FullTime extends Employee{
    private static final double hoursPerWeek = 40;
    public FullTime(String name, String surname, String phoneNumber, String address, String email, LocalDate employmentDate, Contract contract) {
        super(name, surname, phoneNumber, address, email, employmentDate, contract);
    }

    @Override
    public double calculateSalary(Contract contract, LocalDate employmentDate){
        double workingYears = LocalDate.now().getYear()-employmentDate.getYear()+1;
        return switch (contract) {
            case employmentContract -> 0.75 * workingYears*hoursPerWeek*4.5*getBaseSalary();
            case mandateContract -> 0.9 * workingYears*hoursPerWeek*4.5*getBaseSalary();
            case B2B -> workingYears*hoursPerWeek*4.5*getBaseSalary();
        };

    }

}
