package test;

import classes.Reservation;
import classes.ReservationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {
    private Reservation reservation;
    private LocalDateTime testTimestamp;

    @BeforeEach
    void setUp() {
        testTimestamp = LocalDateTime.of(2025, 11, 12, 18, 30);
        Reservation.getReservations().clear();
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
        assertEquals(1, Reservation.getReservations().size());
        assertTrue(Reservation.getReservations().contains(reservation));
    }
}