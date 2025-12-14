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

    //Composition
    private FullTime fullTime;
    private PartTime partTime;

    private HashSet<Shift> shiftsAssigned;

    public Employee(){}

    public Employee(String name, String surname, String phoneNumber, String street, String city, String postalCode, String country, String email, LocalDate employmentDate, Contract contract, Employee manager, Type type, Double salary) throws Exception {
        super(name, surname, phoneNumber, street, city, postalCode, country, email);
        setEmploymentDate(employmentDate);
        setContract(contract);

        managedEmployees = new HashSet<>();
        shiftsAssigned = new HashSet<>();

        if(type == null && salary == null) fullTime = new FullTime();
        else partTime = new PartTime(type, salary);

        if (manager != null) {
            manager.addManagedEmployee(this);
        }
    }

    public void addWorkedInShift(Shift shift) throws Exception {
        if (shift == null) {
            throw new Exception("Shift cannot be null");
        }

        if (!shiftsAssigned.contains(shift)) {
            shiftsAssigned.add(shift);

            if (!shift.getEmployees().contains(this)) {
                shift.addEmployee(this);
            }
        }
    }

    public void removeWorkedInShift(Shift shift) throws Exception {
        if (shift == null) {
            throw new Exception("Shift cannot be null");
        }

        if (shiftsAssigned.remove(shift)) {
            if (shift.getEmployees().contains(this)) {
                shift.removeEmployee(this);
            }
        }
    }
    public void setManager(Employee manager){
        if (this.manager == manager) return;

        if (this.manager != null) {
            this.manager.managedEmployees.remove(this);
        }

        this.manager = manager;

        if (manager != null && !manager.managedEmployees.contains(this)) {
            manager.managedEmployees.add(this);
        }
    }

    public void addManagedEmployee(Employee managed) throws Exception {
        if (managed == null) {
            throw new Exception("Managed employee cannot be null");
        }

        if (managed == this) {
            throw new Exception("An employee cannot manage themselves");
        }

        if (!managedEmployees.contains(managed)) {
            managedEmployees.add(managed);

            if (managed.manager != this) {
                managed.setManager(this);
            }
        }
    }

    public void removeManagedEmployee(Employee managed){

    }

    protected abstract double calculateSalary();

    public double getBaseSalary() {
        return baseSalary;
    }

    public HashSet<Shift> getShiftsAssigned() {
        return shiftsAssigned;
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

    private class FullTime implements IfullTime{
        private static final Double hoursPerWeek = 40.0;

        @Override
        public void ChangeToPartTime(Type type, Double salary) {
            if (partTime != null) {
                throw new IllegalStateException("Employee is already part-time");
            }
            fullTime = null;

            partTime = new PartTime(type, salary);


        }
    }

    private class PartTime implements IpartTime{
        private Type type;
        private Double salary;

        public PartTime(Type type, Double salary){
            this.type = type;
            this.salary = salary;
        }

        @Override
        public void ChangeToFullTime() {
            if (fullTime != null) {
                throw new IllegalStateException("Employee is already full-time");
            }
            partTime = null;

            fullTime = new FullTime();

        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public Double getSalary() {
            return salary;
        }

        public void setSalary(Double salary) {
            this.salary = salary;
        }
    }
}

