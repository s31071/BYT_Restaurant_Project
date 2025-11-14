package test;

import classes.Order;
import classes.OrderStatus;
import classes.Table;
import classes.TableStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    private Order dineInOrder;
    private Table testTable;
    private LocalDateTime testTimestamp;

    @BeforeEach
    void setUp() {
        testTimestamp = LocalDateTime.of(2025, 11, 12, 19, 30);
        testTable = new Table(1, 4, TableStatus.AVAILABLE, testTimestamp);

        dineInOrder = new Order(1, 4, OrderStatus.TAKEN, 3, testTimestamp, testTable);

    }

    @Test
    void testFullConstructor() {
        assertEquals(1, dineInOrder.getId());
        assertEquals(4, dineInOrder.getNumberOfPeople());
        assertEquals(OrderStatus.TAKEN, dineInOrder.getStatus());
        assertEquals(testTable, dineInOrder.getTable());
        assertNotNull(dineInOrder.getTimestamp());
    }
    @Test
    void testFullConstructorIgnoresTimestampParameter() {
        LocalDateTime beforeCreation = LocalDateTime.now();
        Order order = new Order(100, 2, OrderStatus.TAKEN, 1,
                LocalDateTime.of(2020, 1, 1, 0, 0), testTable);
        LocalDateTime afterCreation = LocalDateTime.now();

        assertTrue(order.getTimestamp().isAfter(beforeCreation.minusSeconds(1)));
        assertTrue(order.getTimestamp().isBefore(afterCreation.plusSeconds(1)));
    }
    @Test
    void testGetId() {
        assertEquals(1, dineInOrder.getId());
    }

    @Test
    void testGetNumberOfPeople() {
        assertEquals(4, dineInOrder.getNumberOfPeople());
    }

    @Test
    void testGetStatus() {
        assertEquals(OrderStatus.TAKEN, dineInOrder.getStatus());
    }

    @Test
    void testGetTable() {
        assertEquals(testTable, dineInOrder.getTable());
    }

    @Test
    void testGetTimestamp() {
        assertNotNull(dineInOrder.getTimestamp());
    }

    @Test
    void testSetTimestamp() {
        LocalDateTime newTimestamp = LocalDateTime.of(2024, 12, 1, 10, 0);
        dineInOrder.setTimestamp(newTimestamp);
        assertEquals(newTimestamp, dineInOrder.getTimestamp());
    }

    @Test
    void testSetTimestampNull() {
        dineInOrder.setTimestamp(null);
        assertNull(dineInOrder.getTimestamp());
    }

    @Test
    void testGetTotalPrice() {
        assertEquals(0.0, dineInOrder.getTotalPrice(), 0.001);
    }
    @Test
    void testConstructorWithZeroPeople() {
        Order order = new Order(10, 0, OrderStatus.TAKEN, 1, LocalDateTime.now(), testTable);
        assertEquals(0, order.getNumberOfPeople());
    }

    @Test
    void testConstructorWithNegativePeople() {
        Order order = new Order(10, -5, OrderStatus.TAKEN, 1, LocalDateTime.now(), testTable);
        assertEquals(-5, order.getNumberOfPeople());
    }

    @Test
    void testConstructorWithZeroQuantity() {
        Order order = new Order(10, 2, OrderStatus.TAKEN, 0, LocalDateTime.now(), testTable);
        assertNotNull(order);
    }

    @Test
    void testConstructorWithNegativeQuantity() {
        Order order = new Order(10, 2, OrderStatus.TAKEN, -3, LocalDateTime.now(), testTable);
        assertNotNull(order);
    }

    @Test
    void testConstructorWithNullTable() {
        Order order = new Order(10, 2, OrderStatus.TAKEN, 1, testTimestamp, null);
        assertNull(order.getTable());
    }

    @Test
    void testOrderStatusEnumValues() {
        OrderStatus[] statuses = OrderStatus.values();
        assertEquals(5, statuses.length);

        assertTrue(containsStatus(statuses, OrderStatus.TAKEN));
        assertTrue(containsStatus(statuses, OrderStatus.IN_PREPARATION));
        assertTrue(containsStatus(statuses, OrderStatus.READY));
        assertTrue(containsStatus(statuses, OrderStatus.SERVED));
        assertTrue(containsStatus(statuses, OrderStatus.DELIVERED));
    }

    @Test
    void testOrderStatusValueOf() {
        assertEquals(OrderStatus.TAKEN, OrderStatus.valueOf("TAKEN"));
        assertEquals(OrderStatus.IN_PREPARATION, OrderStatus.valueOf("IN_PREPARATION"));
        assertEquals(OrderStatus.READY, OrderStatus.valueOf("READY"));
        assertEquals(OrderStatus.SERVED, OrderStatus.valueOf("SERVED"));
        assertEquals(OrderStatus.DELIVERED, OrderStatus.valueOf("DELIVERED"));
    }

    @Test
    void testOrderStatusValueOfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            OrderStatus.valueOf("INVALID");
        });
    }

    private boolean containsStatus(OrderStatus[] statuses, OrderStatus target) {
        for (OrderStatus status : statuses) {
            if (status == target) {
                return true;
            }
        }
        return false;
    }
}