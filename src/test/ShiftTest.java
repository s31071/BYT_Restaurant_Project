package test;

import classes.Shift;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ShiftTest {
    private Shift shift;
    private LocalDateTime testDate;
    private LocalDateTime testStartTime;
    private LocalDateTime testEndTime;

    @BeforeEach
    void setUp() throws Exception {

        Method clearShift = Shift.class.getDeclaredMethod("clearExtent");
        clearShift.setAccessible(true);
        clearShift.invoke(null);

        testDate = LocalDateTime.of(2025, 11, 12, 0, 0);
        testStartTime = LocalDateTime.of(2025, 11, 12, 8, 0);
        testEndTime = LocalDateTime.of(2025, 11, 12, 16, 0);
        shift = new Shift("Shift 1", testDate, testStartTime, testEndTime, 5);
    }

    @Test
    void testConstructorInitializesTitle() {
        assertEquals("Shift 1", shift.getTitle());
    }

    @Test
    void testConstructorInitializesDate() {
        assertEquals(testDate, shift.getDate());
    }

    @Test
    void testConstructorInitializesStartTime() {
        assertEquals(testStartTime, shift.getStartTime());
    }

    @Test
    void testConstructorInitializesEndTime() {
        assertEquals(testEndTime, shift.getEndTime());
    }

    @Test
    void testConstructorInitializesNumberOfPeopleNeeded() {
        assertEquals(5, shift.getNumberOfPeopleNeeded());
    }

    @Test
    void testSetNumberOfPeopleNeededUpdatesValue() {
        shift.setNumberOfPeopleNeeded(6);
        assertEquals(6, shift.getNumberOfPeopleNeeded());
    }

    @Test
    void testAddExtent() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 13, 0, 0);
        LocalDateTime start = LocalDateTime.of(2025, 11, 13, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 11, 13, 18, 0);
        Shift newShift = new Shift("Shift 2", date, start, end, 6);

        assertEquals(2, Shift.getExtent().size());
        assertTrue(Shift.getExtent().contains(newShift));
    }

    @Test
    void testAddExtentWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Shift.addExtent(null);
        });
    }

    @Test
    void testGetExtentIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Shift.getExtent().add(shift);
        });
    }

    @Test
    void testRemoveFromExtent() {
        assertTrue(Shift.getExtent().contains(shift));

        Shift.removeFromExtent(shift);
        assertFalse(Shift.getExtent().contains(shift));
    }

    @Test
    void testMultipleShifts() {
        LocalDateTime date2 = LocalDateTime.of(2025, 11, 13, 0, 0);
        LocalDateTime start2 = LocalDateTime.of(2025, 11, 13, 10, 0);
        LocalDateTime end2 = LocalDateTime.of(2025, 11, 13, 18, 0);

        LocalDateTime date3 = LocalDateTime.of(2025, 11, 14, 0, 0);
        LocalDateTime start3 = LocalDateTime.of(2025, 11, 14, 12, 0);
        LocalDateTime end3 = LocalDateTime.of(2025, 11, 14, 20, 0);

        Shift shift2 = new Shift("Shift 3", date2, start2, end2, 4);
        Shift shift3 = new Shift("Shift 4", date3, start3, end3, 7);

        assertEquals(3, Shift.getExtent().size());
        assertTrue(Shift.getExtent().contains(shift));
        assertTrue(Shift.getExtent().contains(shift2));
        assertTrue(Shift.getExtent().contains(shift3));
    }

}