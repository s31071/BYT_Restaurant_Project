package test;

import classes.Order;
import classes.OrderStatus;
import classes.Table;
import classes.TableStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    private Table availableTable;
    private Table takenTable;
    private Table reservedTable;
    private LocalDateTime testDate;

    @BeforeEach
    void setUp() throws Exception {

        Method clearTable = Table.class.getDeclaredMethod("clearExtent");
        clearTable.setAccessible(true);
        clearTable.invoke(null);

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

    @Test
    void testAddExtent() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 13, 18, 0);
        Table newTable = new Table(4, 8, TableStatus.AVAILABLE, date);

        assertEquals(4, Table.getExtent().size());
        assertTrue(Table.getExtent().contains(newTable));
    }

    @Test
    void testAddExtentWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Table.addExtent(null);
        });
    }

    @Test
    void testGetExtentIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Table.getExtent().add(availableTable);
        });
    }

    @Test
    void testRemoveFromExtent() {
        assertTrue(Table.getExtent().contains(availableTable));

        Table.removeFromExtent(availableTable);
        assertFalse(Table.getExtent().contains(availableTable));
        assertEquals(2, Table.getExtent().size());
    }

    @Test
    void testClearExtent() throws Exception {
        assertEquals(3, Table.getExtent().size());

        Method clearTable = Table.class.getDeclaredMethod("clearExtent");
        clearTable.setAccessible(true);
        clearTable.invoke(null);

        assertEquals(0, Table.getExtent().size());
    }

    private boolean containsStatus(TableStatus[] statuses, TableStatus target) {
        for (TableStatus status : statuses) {
            if (status == target) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testTableOrderConnection_AddOrder() {
        Table table = new Table(1, 4, TableStatus.AVAILABLE, LocalDateTime.now());
        LocalDateTime timestamp = LocalDateTime.now();
        Order order = new Order(1, 2, OrderStatus.TAKEN, timestamp, table);

        table.addOrder(order);

        assertEquals(order, table.getOrderByTimestamp(timestamp));
        assertEquals(table, order.getTable());
    }

//    @Test
//    public void testTableOrderConnection_RemoveOrder() {
//        Table table = new Table(1, 4, TableStatus.AVAILABLE, LocalDateTime.now());
//        LocalDateTime timestamp = LocalDateTime.now();
//        Order order = new Order(1, 2, OrderStatus.READY, timestamp, table);
//
//        table.addOrder(order);
//        table.removeOrder(timestamp);
//
//        // Verify forward connection removed
//        assertNull(table.getOrderByTimestamp(timestamp));
//        // Verify reverse connection removed
//        assertNull(order.getTable());
//    }

    @Test
    public void testTableOrderConnection_CannotAddOrderWithSameTimestamp() {
        Table table = new Table(1, 4, TableStatus.AVAILABLE, LocalDateTime.now());
        LocalDateTime timestamp = LocalDateTime.now();
        Order order1 = new Order(1, 2, OrderStatus.IN_PREPARATION, timestamp);
        Order order2 = new Order(2, 3, OrderStatus.READY, timestamp);

        table.addOrder(order1);

        assertThrows(IllegalArgumentException.class, () -> table.addOrder(order2));
    }

    @Test
    public void testTableOrderConnection_MultipleOrders() {
        Table table = new Table(1, 4, TableStatus.AVAILABLE, LocalDateTime.now());
        LocalDateTime timestamp1 = LocalDateTime.now();
        LocalDateTime timestamp2 = LocalDateTime.now().plusMinutes(30);
        Order order1 = new Order(1, 2, OrderStatus.IN_PREPARATION, timestamp1);
        Order order2 = new Order(2, 3, OrderStatus.IN_PREPARATION, timestamp2);

        table.addOrder(order1);
        table.addOrder(order2);

        assertEquals(2, table.getOrders().size());
        assertEquals(order1, table.getOrderByTimestamp(timestamp1));
        assertEquals(order2, table.getOrderByTimestamp(timestamp2));
        assertEquals(table, order1.getTable());
        assertEquals(table, order2.getTable());
    }
}