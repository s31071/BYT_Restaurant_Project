package test;
import classes.Table;
import classes.TableStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    private Table availableTable;
    private Table takenTable;
    private Table reservedTable;
    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDateTime.of(2025, 11, 12, 18, 30);

        availableTable = new Table(1, 4, TableStatus.AVAILABLE, testDate);
        takenTable = new Table(2, 6, TableStatus.TAKEN, testDate);
        reservedTable = new Table(3, 2, TableStatus.RESERVED, testDate);
    }

    @Test
    void testConstructor() {
        assertEquals(1, availableTable.getNumber());
        assertEquals(4, availableTable.getNumberOfSeats());
        assertEquals(TableStatus.AVAILABLE, availableTable.getStatus());
        assertEquals(testDate, availableTable.getDate());
    }

    @Test
    void testConstructorAllStatuses() {
        assertEquals(TableStatus.AVAILABLE, availableTable.getStatus());
        assertEquals(TableStatus.TAKEN, takenTable.getStatus());
        assertEquals(TableStatus.RESERVED, reservedTable.getStatus());
    }

    @Test
    void testConstructorInitializesOrders() {
        assertNotNull(availableTable.getOrders());
        assertEquals(0, availableTable.getOrders().size());
    }

    @Test
    void testGetNumber() {
        assertEquals(1, availableTable.getNumber());
        assertEquals(2, takenTable.getNumber());
        assertEquals(3, reservedTable.getNumber());
    }

    @Test
    void testGetNumberOfSeats() {
        assertEquals(4, availableTable.getNumberOfSeats());
        assertEquals(6, takenTable.getNumberOfSeats());
        assertEquals(2, reservedTable.getNumberOfSeats());
    }

    @Test
    void testGetStatus() {
        assertEquals(TableStatus.AVAILABLE, availableTable.getStatus());
        assertEquals(TableStatus.TAKEN, takenTable.getStatus());
        assertEquals(TableStatus.RESERVED, reservedTable.getStatus());
    }

    @Test
    void testGetDate() {
        assertEquals(testDate, availableTable.getDate());
    }

    @Test
    void testConstructorWithZeroSeats() {
        Table zeroSeatsTable = new Table(10, 0, TableStatus.AVAILABLE, testDate);
        assertEquals(0, zeroSeatsTable.getNumberOfSeats());
    }

    @Test
    void testConstructorWithNegativeNumber() {
        Table negativeNumberTable = new Table(-1, 4, TableStatus.AVAILABLE, testDate);
        assertEquals(-1, negativeNumberTable.getNumber());
    }

    @Test
    void testConstructorWithNullDate() {
        Table nullDateTable = new Table(5, 4, TableStatus.AVAILABLE, null);
        assertNull(nullDateTable.getDate());
    }

    @Test
    void testTableStatusEnumValues() {
        TableStatus[] statuses = TableStatus.values();
        assertEquals(3, statuses.length);

        assertTrue(containsStatus(statuses, TableStatus.AVAILABLE));
        assertTrue(containsStatus(statuses, TableStatus.TAKEN));
        assertTrue(containsStatus(statuses, TableStatus.RESERVED));
    }

    @Test
    void testTableStatusValueOf() {
        assertEquals(TableStatus.AVAILABLE, TableStatus.valueOf("AVAILABLE"));
        assertEquals(TableStatus.TAKEN, TableStatus.valueOf("TAKEN"));
        assertEquals(TableStatus.RESERVED, TableStatus.valueOf("RESERVED"));
    }

    @Test
    void testTableStatusValueOfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            TableStatus.valueOf("INVALID");
        });
    }

    @Test
    void testTableInstanceSeparation() {
        Table table1 = new Table(1, 4, TableStatus.AVAILABLE, testDate);
        Table table2 = new Table(1, 4, TableStatus.AVAILABLE, testDate);

        assertNotSame(table1, table2);
        assertEquals(table1.getNumber(), table2.getNumber());
    }

    @Test
    void testDifferentTableNumbers() {
        assertNotEquals(availableTable.getNumber(), takenTable.getNumber());
        assertNotEquals(takenTable.getNumber(), reservedTable.getNumber());
    }

    private boolean containsStatus(TableStatus[] statuses, TableStatus target) {
        for (TableStatus status : statuses) {
            if (status == target) {
                return true;
            }
        }
        return false;
    }
}
