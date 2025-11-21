package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    static class TestEmployee extends Employee {

        public TestEmployee(String name, String surname, String phoneNumber,
                            String street, String city, String postalCode, String country,
                            String email, LocalDate employmentDate, Contract contract) {
            super(name, surname, phoneNumber, street, city, postalCode, country, email, employmentDate, contract);
        }

        @Override
        protected double calculateSalary() {
            return getBaseSalary() * contractMultiplier(getContract());
        }
    }

    private TestEmployee employee;
    private Address address;
    private LocalDate hireDate;

    @BeforeEach
    void setUp() {
        hireDate = LocalDate.now().minusYears(5);
        employee = new TestEmployee("Jan", "Kowalski", "123456789",
                "Markowskiego", "Piaseczno", "05-500", "Poland",
                "jankowalski@gmail.com", hireDate, Contract.EMPLOYMENT_CONTRACT);

        address = new Address("Markowskiego", "Piaseczno", "05-500", "Poland");
    }

    @Test
    void testConstructor() {
        assertEquals("Jan", employee.getName());
        assertEquals("Kowalski", employee.getSurname());
        assertEquals("123456789", employee.getPhoneNumber());
        assertEquals(address, employee.getAddress());
        assertEquals("jankowalski@gmail.com", employee.getEmail());
        assertEquals(hireDate, employee.getEmploymentDate());
        assertEquals(Contract.EMPLOYMENT_CONTRACT, employee.getContract());
    }

    @Test
    void shouldThrowExceptionForNullEmploymentDate() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestEmployee("Jan", "Kowalski", "123456789",
                        "Markowskiego", "Piaseczno", "05-500", "Poland",
                        "jankowalski@gmail.com", null, Contract.EMPLOYMENT_CONTRACT));
    }

    @Test
    void shouldThrowExceptionForFutureEmploymentDate() {
        LocalDate future = LocalDate.now().plusDays(1);

        assertThrows(IllegalArgumentException.class, () ->
                new TestEmployee("Jan", "Kowalski", "123456789",
                        "Markowskiego", "Piaseczno", "05-500", "Poland",
                        "jankowalski@gmail.com", future, Contract.EMPLOYMENT_CONTRACT));
    }

    @Test
    void shouldThrowExceptionForNullContract() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestEmployee("Jan", "Kowalski", "123456789",
                        "Markowskiego", "Piaseczno", "05-500", "Poland",
                        "jankowalski@gmail.com", hireDate, null));
    }

    @Test
    void setAndGetSalary() {
        employee.setSalary(5000);
        assertEquals(5000, employee.getSalary());
    }

    @Test
    void testYearsWorked() {
        long expected = 5;
        assertEquals(expected, employee.getYearsWorked());
    }

    @Test
    void testContractMultiplier() {
        assertEquals(1.0, employee.contractMultiplier(Contract.EMPLOYMENT_CONTRACT));
        assertEquals(0.85, employee.contractMultiplier(Contract.MANDATE_CONTRACT));
        assertEquals(1.2, employee.contractMultiplier(Contract.B2B));
    }

    @Test
    void shouldUpdateEmployeeUsingPrivateMethod() throws Exception {

        Method method = Employee.class.getDeclaredMethod(
                "updateEmployee", Employee.class, String.class, String.class, String.class,
                Address.class, String.class, LocalDate.class, Contract.class
        );
        method.setAccessible(true);

        Address newAddress = new Address("NewStreet", "Warsaw", "00-001", "Poland");
        LocalDate newDate = LocalDate.now().minusYears(2);

        method.invoke(employee,
                employee,
                "Adam",
                "Nowak",
                "987654321",
                newAddress,
                "adamnowak@gmail.com",
                newDate,
                Contract.B2B
        );

        assertEquals("Adam", employee.getName());
        assertEquals("Nowak", employee.getSurname());
        assertEquals("987654321", employee.getPhoneNumber());
        assertEquals(newAddress, employee.getAddress());
        assertEquals("adamnowak@gmail.com", employee.getEmail());
        assertEquals(newDate, employee.getEmploymentDate());
        assertEquals(Contract.B2B, employee.getContract());
    }
}
