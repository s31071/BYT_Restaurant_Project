package test.java.test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceTest {

    private Address address;
    private ProductOrder productOrder;

    @BeforeEach
    void setup() {
        address = new Address("Street 1", "City", "12345", "Country");
        productOrder = new ProductOrder(2, 3);
    }

    @Test
    void testSetID_validValue() {
        Invoice invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123, "John", address, productOrder);
        invoice.setID(10);
        assertEquals(10, invoice.getID());
    }

    @Test
    void testSetID_negative_throwsException() {
        Invoice invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123, "John", address, productOrder);
        assertThrows(IllegalArgumentException.class, () -> invoice.setID(-5));
    }

    @Test
    void testSetTaxIdentificationNumber_valid() {
        Invoice invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123, "John", address, productOrder);
        invoice.setTaxIdentificationNumber(555);
        assertEquals(555, invoice.getTaxIdentificationNumber());
    }

    @Test
    void testSetTaxIdentificationNumber_invalid_throwsException() {
        Invoice invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123, "John", address, productOrder);
        assertThrows(IllegalArgumentException.class, () -> invoice.setTaxIdentificationNumber(0));
    }

    @Test
    void testSetName_valid() {
        Invoice invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123, "John", address, productOrder);
        invoice.setName("Alice");
        assertEquals("Alice", invoice.getName());
    }

    @Test
    void testSetName_empty_throwsException() {
        Invoice invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123, "John", address, productOrder);
        assertThrows(IllegalArgumentException.class, () -> invoice.setName(""));
    }

    @Test
    void testSetAddress_valid() {
        Invoice invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123, "John", address, productOrder);
        Address newAddress = new Address("A", "B", "C", "D");
        invoice.setAddress(newAddress);
        assertEquals(newAddress, invoice.getAddress());
    }

    @Test
    void testSetAddress_null_throwsException() {
        Invoice invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123, "John", address, productOrder);
        assertThrows(IllegalArgumentException.class, () -> invoice.setAddress(null));
    }

    @Test
    void testSetProductOrder_valid() {
        Invoice invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123, "John", address, productOrder);
        ProductOrder newOrder = new ProductOrder(5, 10);
        invoice.setProductOrder(newOrder);
        assertEquals(newOrder, invoice.getProductOrder());
    }

    @Test
    void testSetProductOrder_null_throwsException() {
        Invoice invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123, "John", address, productOrder);
        assertThrows(IllegalArgumentException.class, () -> invoice.setProductOrder(null));
    }

    @Test
    void testAddSupplyHistory_addsItem() {
        Invoice invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123, "John", address, productOrder);
        SupplyHistory sh = new SupplyHistory(
                LocalDate.now(),
                SupplyHistory.Status.ORDERED,
                invoice,
                productOrder
        );
        invoice.addSupplyHistory(sh);
        assertEquals(2, invoice.getSupplyHistoryList().size());
    }

    @Test
    void testAddSupplyHistory_null_doesNothing() {
        Invoice invoice = new Invoice(Payment.PaymentMethod.CARD, 1, 123, "John", address, productOrder);
        invoice.addSupplyHistory(null);
        assertEquals(0, invoice.getSupplyHistoryList().size());
    }
}