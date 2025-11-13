package test.java.test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceTest {

    private Address address;
    private Address newAddress;
    private ProductOrder productOrder;
    private ProductOrder newProductOrder;
    private Invoice invoice;

    @BeforeEach
    void setup() {
        address = new Address("Koszykowa", "Warsaw", "12345", "Poland");
        productOrder = new ProductOrder(2, 3);

        newAddress = new Address("Koszykowa", "Warsaw", "00-001", "Poland");
        newProductOrder = new ProductOrder(5, 10);

        invoice = new Invoice(
                Payment.PaymentMethod.CARD,
                1,
                123456789,
                "Emilia",
                address,
                productOrder
        );
    }

    @Test
    void testSetIDValid() {
        invoice.setID(10);
        assertEquals(10, invoice.getID());
    }

    @Test
    void testSetIDNegativeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> invoice.setID(-5));
    }

    @Test
    void testSetTaxIdentificationNumberValid() {
        invoice.setTaxIdentificationNumber(987654321);
        assertEquals(987654321, invoice.getTaxIdentificationNumber());
    }

    @Test
    void testSetTaxIdentificationNumberInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> invoice.setTaxIdentificationNumber(0));
    }

    @Test
    void testSetNameValid() {
        invoice.setName("Emilia");
        assertEquals("Emilia", invoice.getName());
    }

    @Test
    void testSetNameEmptyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> invoice.setName(""));
    }

    @Test
    void testSetAddressValid() {
        invoice.setAddress(newAddress);
        assertEquals(newAddress, invoice.getAddress());
    }

    @Test
    void testSetAddressNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> invoice.setAddress(null));
    }

    @Test
    void testSetProductOrderValid() {
        invoice.setProductOrder(newProductOrder);
        assertEquals(newProductOrder, invoice.getProductOrder());
    }

    @Test
    void testSetProductOrderNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> invoice.setProductOrder(null));
    }

    @Test
    void testAddSupplyHistoryValid() {
        SupplyHistory supplyHistory = new SupplyHistory(
                LocalDate.now(),
                SupplyHistory.Status.ORDERED,
                invoice,
                productOrder
        );
        invoice.addSupplyHistory(supplyHistory);
        assertEquals(2, invoice.getSupplyHistoryList().size());
    }

    @Test
    void testAddSupplyHistoryNullDoesNothing() {
        invoice.addSupplyHistory(null);
        assertEquals(0, new Invoice(
                Payment.PaymentMethod.CARD,
                2,
                987654321,
                "Emilia",
                address,
                productOrder
        ).getSupplyHistoryList().size());
    }

    @Test
    void testConstructorNullAddressThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Invoice(Payment.PaymentMethod.CARD, 1, 123456789, "Emilia", null, productOrder));
    }

    @Test
    void testConstructorNullProductOrderThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Invoice(Payment.PaymentMethod.CARD, 1, 123456789, "Emilia", address, null));
    }

    @Test
    void testConstructorNullNameThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Invoice(Payment.PaymentMethod.CARD, 1, 123456789, null, address, productOrder));
    }

    @Test
    void testConstructorNullPaymentMethodThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Invoice(null, 1, 123456789, "Emilia", address, productOrder));
    }
}