package classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Employee extends Person{
    private static List<Employee> employeeList = new ArrayList<>();

    private double salary;
    private LocalDate employmentDate;
    private Contract contract;

    private double baseSalary = 31.5;//dodane przy tworzeniu klasy

    public Employee(String name, String surname, String phoneNumber, String address, String email, LocalDate employmentDate, Contract contract){
        super(name, surname, phoneNumber, address, email);
        this.employmentDate = employmentDate;
        this.contract = contract;

        this.salary = calculateSalary(contract, employmentDate); //derived attribute

        addEmployee(this);
    }

    private static void addEmployee(Employee employee){
        if(employee == null){
            throw new IllegalArgumentException("Employee cannot be null");
        }

        employeeList.add(employee);
    }

    abstract double calculateSalary(Contract contract, LocalDate employmentDate);

    public double getBaseSalary() {
        return baseSalary;
    }

    private void ManageEmployee(){} // co to ma robic?

}

enum Contract{
    employmentContract,
    mandateContract,
    B2B
}
