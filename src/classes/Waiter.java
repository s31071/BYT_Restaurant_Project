package classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Waiter extends Employee{
    private static List<Waiter> waiterList = new ArrayList<>();

    public WorkwearSize workwearSize;
    public double maximumTables;

    public Waiter(String name, String surname, String phoneNumber, String address, String email, LocalDate employmentDate, Contract contract, WorkwearSize workwearSize, double maximumTables) {
        super(name, surname, phoneNumber, address, email, employmentDate, contract);
        this.workwearSize = workwearSize;
        this.maximumTables = maximumTables;

        addWaiter(this);
    }

    @Override
    public double calculateSalary(Contract contract, LocalDate employmentDate) {
        return 0;
    }

    private static void addWaiter(Waiter waiter){
        if(waiter == null){
            throw new IllegalArgumentException("Waiter cannot be null");
        }

        waiterList.add(waiter);
    }

    public static List<Waiter> getWaiterList() {
        return waiterList;
    }

    public static void setWaiterList(List<Waiter> waiterList) {
        Waiter.waiterList = waiterList;
    }

    public WorkwearSize getWorkwearSize() {
        return workwearSize;
    }

    public void setWorkwearSize(WorkwearSize workwearSize) {
        this.workwearSize = workwearSize;
    }

    public double getMaximumTables() {
        return maximumTables;
    }

    public void setMaximumTables(double maximumTables) {
        this.maximumTables = maximumTables;
    }
}

