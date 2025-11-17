package classes;

import java.time.LocalDate;
import java.util.List;
import java.io.Serializable;


public abstract class Employee extends Person implements Serializable {
    private double salary;
    private LocalDate employmentDate;
    private Contract contract;

    private double baseSalary = 31.5;

    public Employee(String name, String surname, String phoneNumber, String address, String email, LocalDate employmentDate, Contract contract){
        super(name, surname, phoneNumber, address, email);
        this.employmentDate = employmentDate;
        this.contract = contract;
    }

    abstract double calculateSalary(Contract contract, LocalDate employmentDate);

    public double getBaseSalary() {
        return baseSalary;
    }

    private void ManageEmployee(ManageEmployeeType type){
        switch (type){
            case UPDATE -> {}
            case DELETE  -> extent.remove(this);
        }
    }

    private void updateEmployee(Employee employee, String newName, String newSurname, String newPhoneNumber, String newAddress, String newEmail, LocalDate newEmploymentDate, Contract newContract){
        employee.setName(newName);
        employee.setSurname(newSurname);
        employee.setPhoneNumber(newPhoneNumber);
        employee.setAddress(newAddress);
        employee.setEmail(newEmail);
        employee.setEmploymentDate(newEmploymentDate);
        employee.setContract(newContract);
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public static void setExtent(List<Employee> employeeList) {
        Employee.extent = employeeList;
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

enum ManageEmployeeType{
    ADD,
    UPDATE,
    DELETE
}