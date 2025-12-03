package test;

import classes.Reservation;
import classes.ReservationStatus;
import classes.Table;
import classes.TableStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {
    private Reservation reservation;
    private LocalDateTime testTimestamp;

    @BeforeEach
    void setUp() throws Exception {
        Field reservationExtent = Reservation.class.getDeclaredField("extent");
        reservationExtent.setAccessible(true);
        ((List<?>) reservationExtent.get(null)).clear();

        testTimestamp = LocalDateTime.of(2025, 11, 12, 18, 30);
        reservation = new Reservation(1, "Ania Szyr", testTimestamp, ReservationStatus.AVAILABLE);
    }

    @Test
    void testConstructorInitializesId() {
        assertEquals(1, reservation.getId());
    }

    @Test
    void testConstructorInitializesNameOfThePerson() {
        assertEquals("Ania Szyr", reservation.getNameOfThePerson());
    }

    @Test
    void testConstructorInitializesTimestamp() {
        assertEquals(testTimestamp, reservation.getTimestamp());
    }

    @Test
    void testConstructorInitializesStatus() {
        assertEquals(ReservationStatus.AVAILABLE, reservation.getStatus());
    }

    @Test
    void testConstructorAddsToReservationsList() {
        assertEquals(1, Reservation.getExtent().size());
        assertTrue(Reservation.getExtent().contains(reservation));
    }

    @Test
    void testAddExtent() {
        LocalDateTime timestamp = LocalDateTime.of(2025, 11, 13, 19, 0);
        Reservation newReservation = new Reservation(2, "Test Person", timestamp, ReservationStatus.AVAILABLE);

        assertTrue(Reservation.getExtent().contains(newReservation));
        assertEquals(2, Reservation.getExtent().size());
    }

    @Test
    void testAddExtentWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Reservation.addExtent(null);
        });
    }

    @Test
    void testGetExtentIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Reservation.getExtent().add(reservation);
        });
    }

    @Test
    void testRemoveFromExtent() {
        assertTrue(Reservation.getExtent().contains(reservation));

        Reservation.removeFromExtent(reservation);
        assertFalse(Reservation.getExtent().contains(reservation));
    }

    @Test
    void testMultipleReservations() {
        LocalDateTime timestamp2 = LocalDateTime.of(2025, 11, 13, 19, 0);
        LocalDateTime timestamp3 = LocalDateTime.of(2025, 11, 14, 20, 0);

        Reservation reservation2 = new Reservation(3, "John Smith", timestamp2, ReservationStatus.RESERVED);
        Reservation reservation3 = new Reservation(4, "Jane Doe", timestamp3, ReservationStatus.TAKEN);

        assertEquals(3, Reservation.getExtent().size());
        assertTrue(Reservation.getExtent().contains(reservation));
        assertTrue(Reservation.getExtent().contains(reservation2));
        assertTrue(Reservation.getExtent().contains(reservation3));
    }

    @Test
    void testClearExtent() throws Exception {
        assertEquals(1, Reservation.getExtent().size());

        Method reservationExtent = Reservation.class.getDeclaredMethod("clearExtent");
        reservationExtent.setAccessible(true);
        reservationExtent.invoke(null);

        assertEquals(0, Reservation.getExtent().size());
    }

    @Test
    public void testReservationTableConnection_AddTable() throws Exception {
        Reservation reservation = new Reservation(1, "Steve Wozniak", LocalDateTime.now(), ReservationStatus.AVAILABLE);
        Table table = new Table(1, 4, TableStatus.AVAILABLE, LocalDateTime.now());

        reservation.addTableAssigned(table);

        assertEquals(table, reservation.getTable());
        assertTrue(table.reservations.contains(reservation));
    }

    @Test
    public void testReservationTableConnection_RemoveTable() throws Exception {
        Reservation reservation = new Reservation(1, "Steve Jobs", LocalDateTime.now(), ReservationStatus.AVAILABLE);
        Table table = new Table(1, 4, TableStatus.AVAILABLE, LocalDateTime.now());

        reservation.addTableAssigned(table);
        reservation.removeTableAssigned();

        assertNull(reservation.getTable());
        assertFalse(table.reservations.contains(reservation));
    }

    @Test
    public void testReservationTableConnection_CannotAddTableTwice() throws Exception {
        Reservation reservation = new Reservation(1, "Ania Szyr", LocalDateTime.now(), ReservationStatus.AVAILABLE);
        Table table1 = new Table(1, 4, TableStatus.AVAILABLE, LocalDateTime.now());
        Table table2 = new Table(2, 2, TableStatus.AVAILABLE, LocalDateTime.now());

        reservation.addTableAssigned(table1);

        assertThrows(Exception.class, () -> reservation.addTableAssigned(table2));
    }

    @Test
    public void testReservationTableConnection_TableMultipleReservations() throws Exception {
        Reservation reservation1 = new Reservation(1, "Ania Szyr", LocalDateTime.now(), ReservationStatus.AVAILABLE);
        Reservation reservation2 = new Reservation(2, "Ania Szyr", LocalDateTime.now().plusDays(1), ReservationStatus.AVAILABLE);
        Table table = new Table(1, 4, TableStatus.AVAILABLE, LocalDateTime.now());

        table.addManagedTableReservation(reservation1);
        table.addManagedTableReservation(reservation2);

        assertTrue(table.reservations.contains(reservation1));
        assertTrue(table.reservations.contains(reservation2));
        assertEquals(table, reservation1.getTable());
        assertEquals(table, reservation2.getTable());
    }
}