package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.io.Serializable;

import static classes.Contract.*;
public class Employee implements Iemployee, Serializable {
    private static List<Employee> extent = new ArrayList<>();

    private double salary;
    private LocalDate employmentDate;
    private Contract contract;
    private double baseSalary = 31.5;

    private Employee manager;
    private HashSet<Employee> managedEmployees;

    private FullTime fullTime;
    private PartTime partTime;

    private Cook cook;
    private Waiter waiter;
    private DeliveryDriver deliveryDriver;

    private Person person;

    private HashSet<Shift> shiftsAssigned;

    public enum Role {
        COOK, WAITER, DELIVERY_DRIVER
    }

    public Employee() {}

    //employee-cook
    public Employee(Person person, LocalDate employmentDate, Contract contract, Employee manager, Type type,
                    double yearsOfExperience, String title, String specialization) throws Exception {
        setPerson(person);
        setEmploymentDate(employmentDate);
        setContract(contract);

        managedEmployees = new HashSet<>();
        shiftsAssigned = new HashSet<>();

        if (type == null) {
            fullTime = new FullTime();
            this.partTime = null;
        } else {
            partTime = new PartTime(type);
            this.fullTime = null;
        }

        if (manager != null) {
            manager.addManagedEmployee(this);
        }
        this.cook = new Cook(this, yearsOfExperience, title, specialization);
        addExtent(this);
    }

    //employee-waiter
    public Employee(Person person, LocalDate employmentDate, Contract contract, Employee manager, Type type,
                    WorkwearSize workwearSize, double maximumTables) throws Exception {
        setPerson(person);
        setEmploymentDate(employmentDate);
        setContract(contract);

        managedEmployees = new HashSet<>();
        shiftsAssigned = new HashSet<>();

        if (type == null) {
            fullTime = new FullTime();
            this.partTime = null;
        } else {
            partTime = new PartTime(type);
            this.fullTime = null;
        }

        if (manager != null) {
            manager.addManagedEmployee(this);
        }
        this.waiter = new Waiter(this, workwearSize, maximumTables);
        addExtent(this);
    }

    //employee-delivery driver
    public Employee(Person person, LocalDate employmentDate, Contract contract, Employee manager, Type type,
                    String carModel, String registrationNumber, boolean bonusApply) throws Exception {
        setPerson(person);
        setEmploymentDate(employmentDate);
        setContract(contract);

        managedEmployees = new HashSet<>();
        shiftsAssigned = new HashSet<>();

        if (type == null) {
            fullTime = new FullTime();
            this.partTime = null;
        } else {
            partTime = new PartTime(type);
            this.fullTime = null;
        }

        if (manager != null) {
            manager.addManagedEmployee(this);
        }

        this.deliveryDriver = new DeliveryDriver(this, carModel, registrationNumber, bonusApply);
        addExtent(this);
    }
    public Cook getCook() {
        return cook;
    }
    void setCook(Cook cook) {
        this.cook = cook;
    }
    public Waiter getWaiter() {
        return waiter;
    }
    void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }
    public DeliveryDriver getDeliveryDriver() {
        return deliveryDriver;
    }
    void setDeliveryDriver(DeliveryDriver deliveryDriver) {
        this.deliveryDriver = deliveryDriver;
    }
    public Role getCurrentRole() {
        if (cook != null) return Role.COOK;
        if (waiter != null) return Role.WAITER;
        if (deliveryDriver != null) return Role.DELIVERY_DRIVER;
        return null;
    }
    public boolean isCook() {
        return cook != null;
    }

    public boolean isWaiter() {
        return waiter != null;
    }

    public boolean isDeliveryDriver() {
        return deliveryDriver != null;
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

    public HashSet<Shift> getShiftsAssigned() {
        return shiftsAssigned;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        if (this.manager == manager) return;

        if (this.manager != null) {
            this.manager.managedEmployees.remove(this);
        }

        this.manager = manager;

        if (manager != null && !manager.managedEmployees.contains(this)) {
            manager.managedEmployees.add(this);
        }
    }

    public HashSet<Employee> getManagedEmployees() {
        return managedEmployees;
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

    public void removeManagedEmployee(Employee managed) throws Exception {
        if (managed == null) {
            throw new Exception("Managed employee cannot be null");
        }

        if (managedEmployees.remove(managed)) {
            if (managed.getManager() == this) {
                managed.removeManager(this);
            }
        }
    }

    public void removeManager(Employee manager) throws Exception {
        if (manager == null) {
            throw new Exception("Manager cannot be null");
        }

        if (this.manager != manager) {
            return;
        }

        this.manager = null;

        if (manager.getManagedEmployees().contains(this)) {
            manager.removeManagedEmployee(this);
        }
    }


    public double calculateSalary() {
        if (cook != null) return cook.calculateSalary();
        if (waiter != null) return waiter.calculateSalary();
        if (deliveryDriver != null) return deliveryDriver.calculateSalary();
        throw new IllegalStateException("Employee must be either a cook, a waiter or a delivery driver");
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public double contractMultiplier(Contract c) {
        return switch (c) {
            case EMPLOYMENT_CONTRACT -> 1.0;
            case MANDATE_CONTRACT -> 0.85;
            case B2B -> 1.2;
        };
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) throws Exception {
        if (person == null) {
            throw new Exception("Person cannot be null");
        }
        this.person = person;
    }

    public String getName() {
        return person.getName();
    }

    public void setName(String name) {
        person.setName(name);
    }

    public String getSurname() {
        return person.getSurname();
    }

    public void setSurname(String surname) {
        person.setSurname(surname);
    }

    public String getPhoneNumber() {
        return person.getPhoneNumber();
    }

    public void setPhoneNumber(String phoneNumber) {
        person.setPhoneNumber(phoneNumber);
    }

    public Address getAddress() {
        return person.getAddress();
    }

    public void setAddress(Address address) {
        person.setAddress(address);
    }

    public String getEmail() {
        return person.getEmail();
    }

    public void setEmail(String email) {
        person.setEmail(email);
    }

    public void setSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        if (employmentDate == null) {
            throw new IllegalArgumentException("Employment date cannot be null");
        }

        if (employmentDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Employment date cannot be in the future");
        }

        this.employmentDate = employmentDate;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setContract(Contract contract) {
        if (contract == null) {
            throw new IllegalArgumentException("Contract cannot be null");
        }
        this.contract = contract;
    }

    public Contract getContract() {
        return contract;
    }

    public long getYearsWorked() {
        return java.time.temporal.ChronoUnit.YEARS.between(getEmploymentDate(), LocalDate.now());
    }

    @Override
    public void updateEmployee(Employee employee, String newName, String newSurname, String newPhoneNumber, 
                               Address newAddress, String newEmail, LocalDate newEmploymentDate, Contract newContract) {
        employee.setName(newName);
        employee.setSurname(newSurname);
        employee.setPhoneNumber(newPhoneNumber);
        employee.setAddress(newAddress);
        employee.setEmail(newEmail);
        employee.setEmploymentDate(newEmploymentDate);
        employee.setContract(newContract);
    }

    public static void addExtent(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        if (extent.contains(employee)) {
            throw new IllegalArgumentException("Such employee is already in data base");
        }
        extent.add(employee);
    }

    public static List<Employee> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Employee employee) {
        extent.remove(employee);
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static void writeExtent(XMLEncoder out) throws IOException {
        out.writeObject(extent);
    }

    public static void readExtent(XMLDecoder in) throws IOException, ClassNotFoundException {
        extent = (List<Employee>) in.readObject();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private class FullTime implements IfullTime {
        private static List<FullTime> extent = new ArrayList<>();
        private static final Double hoursPerWeek = 40.0;

        public FullTime() {
            salary = calculateSalary();
            addExtent(this);
        }

        @Override
        public void ChangeToPartTime(Type type, Double salary) {
            if (partTime != null) {
                throw new IllegalStateException("Employee is already part-time");
            }

            removeFromExtent(this);
            fullTime = null;
            partTime = new PartTime(type);
        }

        public double calculateSalary() {
            double workingYears = getYearsWorked();
            return switch (getContract()) {
                case EMPLOYMENT_CONTRACT -> 0.75 * workingYears * hoursPerWeek * 4.5 * getBaseSalary();
                case MANDATE_CONTRACT -> 0.9 * workingYears * hoursPerWeek * 4.5 * getBaseSalary();
                case B2B -> workingYears * hoursPerWeek * 4.5 * getBaseSalary();
            };
        }

        public static void addExtent(FullTime fullTime) {
            if (fullTime == null) {
                throw new IllegalArgumentException("Full time cannot be null");
            }
            if (extent.contains(fullTime)) {
                throw new IllegalArgumentException("Such full time employee is already in data base");
            }
            extent.add(fullTime);
        }

        public static List<FullTime> getExtent() {
            return Collections.unmodifiableList(extent);
        }

        public static void removeFromExtent(FullTime fullTime) {
            extent.remove(fullTime);
        }

        public static void writeExtent(XMLEncoder out) throws IOException {
            out.writeObject(extent);
        }

        public static void readExtent(XMLDecoder in) throws IOException, ClassNotFoundException {
            extent = (List<FullTime>) in.readObject();
        }

        public static void clearExtent() {
            extent.clear();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FullTime fullTime = (FullTime) o;
            return Employee.this.equals(fullTime.getOuterEmployee());
        }

        @Override
        public int hashCode() {
            return Employee.this.hashCode();
        }

        // helper to access the outer Employee
        private Employee getOuterEmployee() {
            return Employee.this;
        }
    }

    private class PartTime implements IpartTime{
        private static List<PartTime> extent = new ArrayList<>();
        private Type type;

        public PartTime(Type type){
            setType(type);
            salary = calculateSalary();
            addExtent(this);
        }

        @Override
        public void ChangeToFullTime() {
            if (fullTime != null) {
                throw new IllegalStateException("Employee is already full-time");
            }

            removeFromExtent(this);
            partTime = null;
            fullTime = new FullTime();
        }

        public double calculateSalary() {
            return switch (type) {
                case HALF_TIME -> 20 * 4.5 * getBaseSalary();
                case THREE_QUARTER_TIME -> 30 * 4.5 * getBaseSalary();
                case ON_CALL -> 10 * 4.5 * getBaseSalary();
            };
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            if(type == null){
                throw new IllegalArgumentException("Type cannot be null");
            }
            this.type = type;
        }

        public Double getSalary() {
            return salary;
        }

        public static void addExtent(PartTime partTime) {
            if (partTime == null) {
                throw new IllegalArgumentException("Part time cannot be null");
            }
            if(extent.contains(partTime)){
                throw new IllegalArgumentException("Such part time employee is already in data base");
            }
            extent.add(partTime);
        }

        public static List<PartTime> getExtent() {
            return Collections.unmodifiableList(extent);
        }

        public static void removeFromExtent(PartTime partTime) {
            extent.remove(partTime);
        }

        public static void writeExtent(XMLEncoder out) throws IOException {
            out.writeObject(extent);
        }

        public static void readExtent(XMLDecoder in) throws IOException, ClassNotFoundException {
            extent = (List<PartTime>) in.readObject();
        }

        public static void clearExtent(){
            extent.clear();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PartTime partTime = (PartTime) o;
            return Employee.this.equals(partTime.getOuterEmployee());
        }

        @Override
        public int hashCode() {
            return Employee.this.hashCode();
        }

        private Employee getOuterEmployee() {
            return Employee.this;
        }
    }
}