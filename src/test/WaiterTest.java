package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class WaiterTest {

    private Waiter waiter;

    @BeforeEach
    void setUp() throws Exception {
        Method clearMethod = Waiter.class.getDeclaredMethod("clearExtent");
        clearMethod.setAccessible(true);
        clearMethod.invoke(null);
        waiter = new Waiter("Tomasz", "Lis", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "tomasz@example.com",
                LocalDate.now(), Contract.EMPLOYMENT_CONTRACT, WorkwearSize.M, 10, null);
    }

    @Test
    void testConstructor() {
        assertEquals("Tomasz", waiter.getName());
        assertEquals("Lis", waiter.getSurname());
        assertEquals(10, waiter.getMaximumTables());
    }

    @Test
    void testCalculateSalaryAlwaysZero() {
        assertEquals(5492.0, waiter.calculateSalary());
    }

    @Test
    void testNullWaiterAdd() throws Exception {
        var method = Waiter.class.getDeclaredMethod("addExtent", Waiter.class);
        method.setAccessible(true);
        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, (Object) null));

        assertTrue(ex.getTargetException() instanceof IllegalArgumentException);
        assertEquals("Waiter cannot be null", ex.getTargetException().getMessage());
    }

    @Test
    void shouldThrowExceptionForNullWorkwearSize() {
        assertThrows(IllegalArgumentException.class, () ->
                new Waiter("Tomasz", "Lis", "123456789", "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "tomaszlis@gmail.com", LocalDate.now(),
                        Contract.EMPLOYMENT_CONTRACT, null, 10, null));
    }

    @Test
    void shouldThrowExceptionForZeroMaximumTables() {
        assertThrows(IllegalArgumentException.class, () ->
                new Waiter("Tomasz", "Lis", "123456789", "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "tomaszlis@gmail.com", LocalDate.now(),
                        Contract.EMPLOYMENT_CONTRACT, WorkwearSize.M, 0, null));
    }

    @Test
    void shouldThrowExceptionForNegativeMaximumTables() {
        assertThrows(IllegalArgumentException.class, () ->
                new Waiter("Tomasz", "Lis", "123456789", "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "tomaszlis@egmail.com", LocalDate.now(),
                        Contract.EMPLOYMENT_CONTRACT, WorkwearSize.M, -5, null));
    }

    @Test
    void shouldThrowExceptionForTooManyTables() {
        assertThrows(IllegalArgumentException.class, () ->
                new Waiter("Tomasz", "Lis", "123456789", "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "tomaszlis@gmail.com", LocalDate.now(),
                        Contract.EMPLOYMENT_CONTRACT, WorkwearSize.M, 30, null));
    }

    @Test
    void testSetWorkwearSizeValid() {
        waiter.setWorkwearSize(WorkwearSize.L);
        assertEquals(WorkwearSize.L, waiter.getWorkwearSize());
    }

    @Test
    void testSetMaximumTablesValidLowerRange() {
        waiter.setMaximumTables(1);
        assertEquals(1, waiter.getMaximumTables());
    }

    @Test
    void testSetMaximumTablesValid() {
        waiter.setMaximumTables(15);
        assertEquals(15, waiter.getMaximumTables());
    }

    @Test
    void testCalculateSalaryIncreasesWithTables() {
        double baseSalary = waiter.calculateSalary();
        waiter.setMaximumTables(20);
        double higherSalary = waiter.calculateSalary();
        assertTrue(higherSalary > baseSalary);
    }

    @Test
    void testCalculateSalaryIncreasesWithYearsWorked() throws Exception {
        Waiter w = new Waiter("Anna", "Kowal", "987654321", "Dluga", "Warszawa", "00-001",
                "Poland", "annakowal@gmail.com", LocalDate.now().minusYears(5),
                Contract.EMPLOYMENT_CONTRACT, WorkwearSize.S, 10, null);

        double salary = w.calculateSalary();
        assertTrue(salary > waiter.calculateSalary());
    }

    @Test
    void testAddExtentDuplicateWaiterThrowsException() throws Exception {
        Method method = Waiter.class.getDeclaredMethod("addExtent", Waiter.class);
        method.setAccessible(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Waiter(
                        "Tomasz", "Lis", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "tomasz@example.com",
                        LocalDate.now(), Contract.EMPLOYMENT_CONTRACT, WorkwearSize.M, 10, null
                )
        );

        assertEquals("Such waiter is already in data base", ex.getMessage());
    }

    @Test
    void testExtentContainsOnlyOneWaiterAfterDuplicateAttempt() throws Exception {
        Method addMethod = Waiter.class.getDeclaredMethod("addExtent", Waiter.class);
        addMethod.setAccessible(true);

        Method getExtentMethod = Waiter.class.getDeclaredMethod("getExtent");
        getExtentMethod.setAccessible(true);

        try {
            Waiter duplicate = new Waiter("Tomasz", "Lis", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "tomasz@example.com",
                    LocalDate.now(), Contract.EMPLOYMENT_CONTRACT, WorkwearSize.M, 10, null);
        } catch (IllegalArgumentException ignored) {}

        var extent = (java.util.List<Waiter>) getExtentMethod.invoke(null);
        assertEquals(1, extent.size());
        assertEquals(waiter, extent.get(0));
    }

    @Test
    void testRemoveFromExtent() throws Exception {
        Waiter w2 = new Waiter(
                "Anna", "Szyr", "987654321",
                "Lechicka", "Warszawa", "00-001", "Poland",
                "annaszyr@gmail.com", LocalDate.now(),
                Contract.EMPLOYMENT_CONTRACT, WorkwearSize.S, 8, null
        );

        Method getExtent = Waiter.class.getDeclaredMethod("getExtent");
        getExtent.setAccessible(true);

        var extent = (java.util.List<Waiter>) getExtent.invoke(null);
        assertEquals(2, extent.size());
        assertTrue(extent.contains(w2));

        Waiter.removeFromExtent(w2);

        extent = (java.util.List<Waiter>) getExtent.invoke(null);
        assertEquals(1, extent.size());
        assertFalse(extent.contains(w2));
        assertTrue(extent.contains(waiter));
    }

    @Test
    void testClearExtent() throws Exception {
        Waiter w2 = new Waiter(
                "Eva", "Nowak", "555444333",
                "Koszykowa", "Warszawa", "00-001", "Poland",
                "s31431@gmail.com", LocalDate.now(),
                Contract.EMPLOYMENT_CONTRACT, WorkwearSize.L, 12, null
        );

        Waiter w3 = new Waiter(
                "Adam", "Nowak", "222333444",
                "Nowogrodzka", "Warszawa", "00-002", "Poland",
                "adamnowak@gmail.com", LocalDate.now(),
                Contract.EMPLOYMENT_CONTRACT, WorkwearSize.M, 14, null
        );

        Method getExtent = Waiter.class.getDeclaredMethod("getExtent");
        getExtent.setAccessible(true);

        var extent = (java.util.List<Waiter>) getExtent.invoke(null);
        assertEquals(3, extent.size());
        assertTrue(extent.contains(w2));
        assertTrue(extent.contains(w3));
        assertTrue(extent.contains(waiter));

        Method clearMethod = Waiter.class.getDeclaredMethod("clearExtent");
        clearMethod.setAccessible(true);
        clearMethod.invoke(null);

        extent = (java.util.List<Waiter>) getExtent.invoke(null);
        assertEquals(0, extent.size());
    }

    @Test
    void testAddManagedReservation() throws Exception {
        Reservation reservation = new Reservation(
                1,
                "John Doe",
                LocalDateTime.now(),
                ReservationStatus.AVAILABLE
        );

        waiter.addManagedReservation(reservation);

        assertTrue(waiter.getReservations().contains(reservation));

        assertEquals(waiter, reservation.getWaiterAssigned());
    }

    @Test
    void testAddManagedReservationNullThrowsException() {
        Exception ex = assertThrows(Exception.class, () ->
                waiter.addManagedReservation(null)
        );

        assertEquals("Reservation cannot be null", ex.getMessage());
    }

    @Test
    void testRemoveManagedReservation() throws Exception {
        Reservation reservation = new Reservation(
                2,
                "Anna",
                LocalDateTime.now(),
                ReservationStatus.AVAILABLE
        );

        waiter.addManagedReservation(reservation);

        waiter.removeManagedReservation(reservation);

        assertFalse(waiter.getReservations().contains(reservation));
        assertNull(reservation.getWaiterAssigned());
    }

    @Test
    void testRemoveManagedReservationNotAssignedDoesNothing() {
        Reservation reservation = new Reservation(
                3,
                "Mike",
                LocalDateTime.now(),
                ReservationStatus.AVAILABLE
        );

        assertTrue(waiter.getReservations().isEmpty());

        assertDoesNotThrow(() ->
                waiter.removeManagedReservation(reservation)
        );

        assertTrue(waiter.getReservations().isEmpty());
    }


    @Test
    void testBidirectionalRelationAfterAdd() throws Exception {
        Reservation reservation = new Reservation(
                4,
                "Client",
                LocalDateTime.now(),
                ReservationStatus.AVAILABLE
        );

        waiter.addManagedReservation(reservation);

        assertTrue(waiter.getReservations().contains(reservation));
        assertEquals(waiter, reservation.getWaiterAssigned());
    }


}

