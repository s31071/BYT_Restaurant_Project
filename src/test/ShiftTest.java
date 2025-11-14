package test;
import classes.Shift;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ShiftTest {
    private Shift shift;
    private LocalDateTime testDate;
    private LocalDateTime testStartTime;
    private LocalDateTime testEndTime;

    @BeforeEach
    void setUp() {
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
}
