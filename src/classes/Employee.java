package classes;

import java.time.LocalDate;
import java.util.List;
import java.io.Serializable;


public abstract class Employee extends Person implements Serializable {
    private double salary;
    private LocalDate employmentDate;
    private Contract contract;

    private double baseSalary = 31.5;

    public Employee(){}

    public Employee(String name, String surname, String phoneNumber, String street, String city, String postalCode, String country, String email, LocalDate employmentDate, Contract contract){
        super(name, surname, phoneNumber, street, city, postalCode, country, email);
        setEmploymentDate(employmentDate);
        setContract(contract);
    }

    protected abstract double calculateSalary();

    public double getBaseSalary() {
        return baseSalary;
    }

    private void updateEmployee(Employee employee, String newName, String newSurname, String newPhoneNumber, Address newAddress, String newEmail, LocalDate newEmploymentDate, Contract newContract){
        employee.setName(newName);
        employee.setSurname(newSurname);
        employee.setPhoneNumber(newPhoneNumber);
        employee.setAddress(newAddress);
        employee.setEmail(newEmail);
        employee.setEmploymentDate(newEmploymentDate);
        employee.setContract(newContract);
    }

    public double contractMultiplier(Contract c) {
        return switch (c) {
            case EMPLOYMENT_CONTRACT -> 1.0;
            case MANDATE_CONTRACT -> 0.85;
            case B2B -> 1.2;
        };
    }

    public void setSalary(double salary) {
        if(salary < 0){
            throw new IllegalArgumentException("Salary cannot be 0");
        }
        this.salary = salary;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        if(employmentDate == null){
            throw new IllegalArgumentException("Employment date cannot be null");
        }

        if(employmentDate.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Employment date cannot be in the future");
        }

        this.employmentDate = employmentDate;
    }

    public void setContract(Contract contract) {
        if(contract == null){
            throw new IllegalArgumentException("Contract cannot be null");
        }
        this.contract = contract;
    }

    public long getYearsWorked() {
        return java.time.temporal.ChronoUnit.YEARS.between(getEmploymentDate(), LocalDate.now());
    }


    public double getSalary() {
        return salary;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public Contract getContract() {
        return contract;
    }
}

