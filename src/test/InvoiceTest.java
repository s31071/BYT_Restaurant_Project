package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceTest {

    private Address address;
    private Address newAddress;
    private ProductOrder productOrder;
    private ProductOrder newProductOrder;
    private Invoice invoice;

    @BeforeEach
    void setup() throws Exception {

        Method clearProduct = Product.class.getDeclaredMethod("clearExtent");
        clearProduct.setAccessible(true);
        clearProduct.invoke(null);

        Method clearProductOrder = ProductOrder.class.getDeclaredMethod("clearExtent");
        clearProductOrder.setAccessible(true);
        clearProductOrder.invoke(null);

        Method clearInvoice = Invoice.class.getDeclaredMethod("clearExtent");
        clearInvoice.setAccessible(true);
        clearInvoice.invoke(null);

        Product p1 = new Product(1, "Milk", 1.0, Category.DAIRY, null, 10.0);
        Product p2 = new Product(2, "Bread", 0.5, Category.DAIRY, null, 5.0);
        productOrder = new ProductOrder(List.of(p1, p2));

        Product p3 = new Product(3, "Butter", 0.2, Category.DAIRY, null, 12.0);
        Product p4 = new Product(4, "Eggs", 0.6, Category.DAIRY, null, 7.0);
        newProductOrder = new ProductOrder(List.of(p3, p4));

        address = new Address("Koszykowa", "Warsaw", "12345", "Poland");
        newAddress = new Address("Koszykowa", "Warsaw", "00-001", "Poland");

        invoice = new Invoice(PaymentMethod.CARD, 10, 123456789, "Emilia", "Koszykowa", "Warsaw", "12345", "Poland", productOrder);
    }

    @Test
    void testFullConstructor() {
        assertEquals(10, invoice.getID());
        assertEquals(123456789, invoice.getTaxIdentificationNumber());
        assertEquals("Emilia", invoice.getName());
        assertEquals(address, invoice.getAddress());
        assertEquals(productOrder, invoice.getProductOrder());
        assertEquals(productOrder.getTotalSum(), invoice.getSum());
    }

    @Test
    void testConstructorAddsToExtent() {
        assertEquals(1, Invoice.getExtent().size());
        assertTrue(Invoice.getExtent().contains(invoice));
    }

    @Test
    void testSetIDValid() {
        invoice.setID(20);
        assertEquals(20, invoice.getID());
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
        invoice.setName("Anna");
        assertEquals("Anna", invoice.getName());
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
        assertEquals(newProductOrder.getTotalSum(), invoice.getSum());
    }

    @Test
    void testSetProductOrderNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> invoice.setProductOrder(null));
    }


    @Test
    void testConstructorNullProductOrderThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Invoice(PaymentMethod.CARD, 20, 999999999, "Emilia", "Koszykowa", "Warsaw", "12345", "Poland", null));
    }

    @Test
    void testConstructorNullNameThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Invoice(PaymentMethod.CARD, 20, 999999999, null, "Koszykowa", "Warsaw", "12345", "Poland", productOrder));
    }

    @Test
    void testConstructorNullPaymentMethodThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Invoice(null, 20, 999999999, "Emilia", "Koszykowa", "Warsaw", "12345", "Poland", productOrder));
    }

    @Test
    void testGetSumReflectsProductOrder() {
        assertEquals(productOrder.getTotalSum(), invoice.getSum());
    }

    @Test
    void testSumUpdatesWhenProductOrderChanges() {
        invoice.setProductOrder(newProductOrder);
        assertEquals(newProductOrder.getTotalSum(), invoice.getSum());
    }

    @Test
    void testAddExtent() {
        Invoice invoice1 = new Invoice(PaymentMethod.CASH, 30, 555555555, "Anna", "Koszykowa", "Warsaw", "00-001", "Poland", newProductOrder);
        assertTrue(Invoice.getExtent().contains(invoice1));
        assertEquals(2, Invoice.getExtent().size());
    }

    @Test
    void testAddExtentWithNull() {
        assertThrows(IllegalArgumentException.class, () -> Invoice.addExtent(null));
    }

    @Test
    void testAddExtentDuplicateThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Invoice.addExtent(invoice));
    }

    @Test
    void testGetExtentIsUnmodifiable() {
        Invoice valid = new Invoice(PaymentMethod.VOUCHER, 50, 888888888, "John", "Koszykowa", "Warsaw", "12345", "Poland", newProductOrder);
        assertThrows(UnsupportedOperationException.class, () ->
                Invoice.getExtent().add(valid));
    }

    @Test
    void testRemoveFromExtent() {
        Invoice invoiceToRemove =
                new Invoice(PaymentMethod.CARD, 40, 999888777, "Mark", "Koszykowa", "Warsaw", "12345", "Poland", newProductOrder);

        assertTrue(Invoice.getExtent().contains(invoiceToRemove));

        Invoice.removeFromExtent(invoiceToRemove);
        assertFalse(Invoice.getExtent().contains(invoiceToRemove));
    }

    @Test
    void testMultipleInvoices() {
        Invoice i1 = new Invoice(PaymentMethod.CASH, 100, 111111111, "A", "Koszykowa", "Warsaw", "12345", "Poland", productOrder);
        Invoice i2 = new Invoice(PaymentMethod.CARD, 200, 222222222, "B", "Koszykowa", "Warsaw", "12345", "Poland", newProductOrder);

        assertEquals(3, Invoice.getExtent().size());
        assertTrue(Invoice.getExtent().contains(invoice));
        assertTrue(Invoice.getExtent().contains(i1));
        assertTrue(Invoice.getExtent().contains(i2));
    }

    @Test
    void testClearExtentWorksProperly() throws Exception {
        Invoice i1 = new Invoice(PaymentMethod.CASH, 300, 333333333, "Adam", "Koszykowa", "Warsaw", "12345", "Poland", productOrder);
        Invoice i2 = new Invoice(PaymentMethod.CARD, 400, 444444444, "Eva", "Koszykowa", "Warsaw", "12345", "Poland", newProductOrder);

        assertEquals(3, Invoice.getExtent().size());
        assertTrue(Invoice.getExtent().contains(i1));
        assertTrue(Invoice.getExtent().contains(i2));

        Method clearInvoice = Invoice.class.getDeclaredMethod("clearExtent");
        clearInvoice.setAccessible(true);
        clearInvoice.invoke(null);

        assertEquals(0, Invoice.getExtent().size());
    }
}