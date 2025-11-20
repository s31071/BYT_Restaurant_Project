package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceTest {

    private Address address;
    private Address newAddress;
    private ProductOrder productOrder;
    private ProductOrder newProductOrder;
    private Invoice invoice;
    private Category category;

    @BeforeEach
    void setup() throws Exception {
        Field addrExtent = Address.class.getDeclaredField("extent");
        addrExtent.setAccessible(true);
        ((List<?>) addrExtent.get(null)).clear();

        Field prodExtent = Product.class.getDeclaredField("extent");
        prodExtent.setAccessible(true);
        ((List<?>) prodExtent.get(null)).clear();

        Field orderExtent = ProductOrder.class.getDeclaredField("extent");
        orderExtent.setAccessible(true);
        ((List<?>) orderExtent.get(null)).clear();

        Field invoiceExtent = Invoice.class.getDeclaredField("extent");
        invoiceExtent.setAccessible(true);
        ((List<?>) invoiceExtent.get(null)).clear();

        Product p1 = new Product(1, "Milk", 1.0, Category.DAIRY);
        Product p2 = new Product(2, "Bread", 0.5, Category.DAIRY);
        productOrder = new ProductOrder(List.of(p1, p2));

        Product p3 = new Product(3, "Butter", 0.2, Category.DAIRY);
        Product p4 = new Product(4, "Eggs", 0.6, Category.DAIRY);
        newProductOrder = new ProductOrder(List.of(p3, p4));

        address = new Address("Koszykowa", "Warsaw", "12345", "Poland");
        newAddress = new Address("Koszykowa", "Warsaw", "00-001", "Poland");

        invoice = new Invoice(PaymentMethod.CARD, 1, 123456789, "Emilia", address, productOrder
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
    void testConstructorNullAddressThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Invoice(PaymentMethod.CARD, 1, 123456789, "Emilia", null, productOrder));
    }

    @Test
    void testConstructorNullProductOrderThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Invoice(PaymentMethod.CARD, 1, 123456789, "Emilia", address, null));
    }

    @Test
    void testConstructorNullNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Invoice(PaymentMethod.CARD, 1, 123456789, null, address, productOrder));
    }

    @Test
    void testConstructorNullPaymentMethodThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Invoice(null, 1, 123456789, "Emilia", address, productOrder));
    }

    @Test
    void testAddExtent() {
        Invoice invoice1 = new Invoice(PaymentMethod.CASH, 1, 123456789, "Emilia", address, productOrder);
        assertTrue(Invoice.getExtent().contains(invoice1));
    }

    @Test
    void testAddExtentWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Invoice.addExtent(null);
        });
    }

    @Test
    void testGetExtentIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Invoice.getExtent().add(new Invoice(PaymentMethod.VOUCHER, 1, 123456789, "Emilia", address, productOrder));
        });
    }

    @Test
    void testRemoveFromExtent() {
        Invoice invoiceToRemove = new Invoice(PaymentMethod.CARD, 1, 123456789, "Emilia", address, productOrder);
        assertTrue(Invoice.getExtent().contains(invoiceToRemove));

        Invoice.removeFromExtent(invoiceToRemove);
        assertFalse(Invoice.getExtent().contains(invoiceToRemove));
    }
}