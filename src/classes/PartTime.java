package classes;

import java.time.LocalDate;

public class PartTime extends Employee{
    private Type type;
    public PartTime(String name, String surname, String phoneNumber, String address, String email, LocalDate employmentDate, Contract contract, Type type) {
        super(name, surname, phoneNumber, address, email, employmentDate, contract);
        this.type = type;
    }

    @Override
    double calculateSalary(Contract contract, LocalDate employmentDate) {
        return switch (type){
            case halfTime -> 20*4.5*getBaseSalary();
            case threeQuarterTime -> 30*4.5*getBaseSalary();
            case onCall -> 10*4.5*getBaseSalary();
        };

    }
}

enum Type{
    halfTime,
    threeQuarterTime,
    onCall
}
