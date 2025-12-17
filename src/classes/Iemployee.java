package classes;

import java.time.LocalDate;

public interface Iemployee {
    double getSalary();
    void setSalary(double salary);

    LocalDate getEmploymentDate();
    void setEmploymentDate(LocalDate employmentDate);

    Contract getContract();
    void setContract(Contract contract);

    double calculateSalary();
    void updateEmployee(Employee employee, String newName, String newSurname, String newPhoneNumber, Address newAddress, String newEmail, LocalDate newEmploymentDate, Contract newContract);
}
