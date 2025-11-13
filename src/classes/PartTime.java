package classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PartTime extends Employee{
    private static List<PartTime> partTimeList = new ArrayList<>();

    public Type type;
    public double salary;
    public PartTime(String name, String surname, String phoneNumber, String address, String email, LocalDate employmentDate, Contract contract, Type type) {
        super(name, surname, phoneNumber, address, email, employmentDate, contract);
        this.type = type;
        this.salary = calculateSalary(contract, employmentDate);

        addPartTime(this);
    }

    @Override
    public double calculateSalary(Contract contract, LocalDate employmentDate) {
        return switch (type){
            case halfTime -> 20*4.5*getBaseSalary();
            case threeQuarterTime -> 30*4.5*getBaseSalary();
            case onCall -> 10*4.5*getBaseSalary();
        };

    }

    public static void addPartTime(PartTime partTime){
        if(partTime == null){
            throw new IllegalArgumentException("Part time cannot be null");
        }

        partTimeList.add(partTime);
    }

    public static List<PartTime> getPartTimeList() {
        return partTimeList;
    }

    public static void setPartTimeList(List<PartTime> partTimeList) {
        PartTime.partTimeList = partTimeList;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

