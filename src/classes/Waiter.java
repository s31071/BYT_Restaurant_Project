package classes;

import java.time.LocalDate;

public class Waiter extends Employee{
    private WorkwearSize workwearSize;
    private double maximumTables;

    public Waiter(String name, String surname, String phoneNumber, String address, String email, LocalDate employmentDate, Contract contract, WorkwearSize workwearSize, double maximumTables) {
        super(name, surname, phoneNumber, address, email, employmentDate, contract);
        this.workwearSize = workwearSize;
        this.maximumTables = maximumTables;
    }

    @Override
    double calculateSalary(Contract contract, LocalDate employmentDate) {
        return 0;
    }
}

enum WorkwearSize{
    s,
    m,
    l
}
