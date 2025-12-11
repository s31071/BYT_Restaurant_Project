package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    static class TestEmployee extends Employee {

        public TestEmployee(String name, String surname, String phoneNumber,
                            String street, String city, String postalCode, String country,
                            String email, LocalDate employmentDate, Contract contract, Employee manager) throws Exception {
            super(name, surname, phoneNumber, street, city, postalCode, country, email, employmentDate, contract, manager);
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
    void setUp() throws Exception {
        hireDate = LocalDate.now().minusYears(5);
        employee = new TestEmployee("Jan", "Kowalski", "123456789",
                "Markowskiego", "Piaseczno", "05-500", "Poland",
                "jankowalski@gmail.com", hireDate, Contract.EMPLOYMENT_CONTRACT, null);

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
                        "jankowalski@gmail.com", null, Contract.EMPLOYMENT_CONTRACT, null));
    }

    @Test
    void shouldThrowExceptionForFutureEmploymentDate() {
        LocalDate future = LocalDate.now().plusDays(1);

        assertThrows(IllegalArgumentException.class, () ->
                new TestEmployee("Jan", "Kowalski", "123456789",
                        "Markowskiego", "Piaseczno", "05-500", "Poland",
                        "jankowalski@gmail.com", future, Contract.EMPLOYMENT_CONTRACT, null));
    }

    @Test
    void shouldThrowExceptionForNullContract() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestEmployee("Jan", "Kowalski", "123456789",
                        "Markowskiego", "Piaseczno", "05-500", "Poland",
                        "jankowalski@gmail.com", hireDate, null, null));
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

    @Test
    void testAddWorkedInShift() throws Exception {
        Shift shift = new Shift(
                "Morning Shift",
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now().plusHours(8),
                4
        );
        shift.setEmployees(new HashSet<>());

        employee.addWorkedInShift(shift);

        assertTrue(shift.getEmployees().contains(employee));
        var field = Employee.class.getDeclaredField("shiftsAssigned");
        field.setAccessible(true);
        HashSet<Shift> shifts = (HashSet<Shift>) field.get(employee);

        assertTrue(shifts.contains(shift));
    }

    @Test
    void testAddWorkedInShiftShouldThrowExceptionForNull() {
        assertThrows(Exception.class, () -> employee.addWorkedInShift(null));
    }

    @Test
    void testRemoveWorkedInShift() throws Exception {
        Shift shift = new Shift(
                "Evening Shift",
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now().plusHours(6),
                4
        );
        shift.setEmployees(new HashSet<>());
        employee.addWorkedInShift(shift);

        employee.removeWorkedInShift(shift);

        assertFalse(shift.getEmployees().contains(employee));

        var field = Employee.class.getDeclaredField("shiftsAssigned");
        field.setAccessible(true);
        HashSet<Shift> shifts = (HashSet<Shift>) field.get(employee);

        assertFalse(shifts.contains(shift));
    }

    @Test
    void testRemoveWorkedInShiftShouldThrowExceptionForNull() {
        assertThrows(Exception.class, () -> employee.removeWorkedInShift(null));
    }

    @Test
    void testAddManagedEmployee() throws Exception {
        TestEmployee manager = new TestEmployee(
                "Manager", "Boss", "111222333",
                "Street", "City", "00-000", "Poland",
                "manager@gmail.com", hireDate, Contract.EMPLOYMENT_CONTRACT, null
        );

        TestEmployee subordinate = new TestEmployee(
                "Worker", "Helper", "444555666",
                "Street", "City", "00-000", "Poland",
                "worker@gmail.com", hireDate, Contract.EMPLOYMENT_CONTRACT, null
        );

        manager.addManagedEmployee(subordinate);

        var managerField = Employee.class.getDeclaredField("manager");
        managerField.setAccessible(true);
        Employee assignedManager = (Employee) managerField.get(subordinate);

        assertEquals(manager, assignedManager);

        var managedField = Employee.class.getDeclaredField("managedEmployees");
        managedField.setAccessible(true);
        HashSet<Employee> managedSet = (HashSet<Employee>) managedField.get(manager);

        assertTrue(managedSet.contains(subordinate));
    }

}
