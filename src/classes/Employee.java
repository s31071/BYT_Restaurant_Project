package classes;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.io.Serializable;


public abstract class Employee extends Person implements Serializable {
    private double salary;
    private LocalDate employmentDate;
    private Contract contract;
    private double baseSalary = 31.5;

    //reflex association
    private Employee manager;
    private HashSet<Employee> managedEmployees;

    private HashSet<Shift> shiftsAssigned;

    public Employee(){}

    public Employee(String name, String surname, String phoneNumber, String street, String city, String postalCode, String country, String email, LocalDate employmentDate, Contract contract, Employee manager){
        super(name, surname, phoneNumber, street, city, postalCode, country, email);
        setEmploymentDate(employmentDate);
        setContract(contract);

        if(manager == null){
            managedEmployees = new HashSet<>();
        }
        shiftsAssigned = new HashSet<>();
    }

    public void addWorkedInShift(Shift shift) throws Exception {
        if(shift == null){
            throw new Exception("Shift cannot be null");
        }
        shiftsAssigned.add(shift);
        shift.getEmployees().add(this);
    }

    public void removeWorkedInShift(Shift shift) throws Exception {
        if(shift == null){
            throw new Exception("Shift cannot be null");
        }
        shiftsAssigned.remove(shift);
        shift.getEmployees().remove(this);
    }
    public void setManager(Employee manager){
        this.manager = manager;
    }

    public void addManagedEmployee(Employee managed) throws Exception {
        if(manager == null){
            managedEmployees.add(managed);
            managed.setManager(this);
        }else{
            throw new Exception("An employee who already has manager cannot be a manager of any other employees");
        }
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

