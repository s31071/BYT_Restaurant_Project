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

    private void ManageEmployee(ManageEmployeeType type){
      switch (type){
          case update -> {
              //updateEmployee(this, );
          }
          case delete -> employeeList.remove(this);
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
}

enum ManageEmployeeType{
    add,
    update,
    delete
}

enum Contract{
    employmentContract,
    mandateContract,
    B2B
}
