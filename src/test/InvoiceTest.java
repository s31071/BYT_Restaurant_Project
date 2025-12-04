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

    private ProductOrder po1;
    private ProductOrder po2;

    private Invoice invoice;

    Supplier supplier1;
    Supplier supplier2;

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

        Method clearSupplyHistory = SupplyHistory.class.getDeclaredMethod("clearExtent");
        clearSupplyHistory.setAccessible(true);
        clearSupplyHistory.invoke(null);

        supplier1 = new Supplier(
                "Anna","Szyr","111222333",
                "Koszykowa","Warsaw","00-000","Poland",
                "annaszyr@gmail.com","Company1", Category.DAIRY, 10.0
        );

        supplier2 = new Supplier(
                "Anna","Szyr","444555666",
                "Nowogrodzka","Warsaw","00-000","Poland",
                "annaszyr@gmail.com","Company2", Category.DAIRY, 15.0
        );

        Product p1 = new Product(1, "Milk", 1.0, Category.DAIRY, null, 10.0);
        Product p2 = new Product(2, "Bread", 0.5, Category.DAIRY, null, 5.0);
        po1 = new ProductOrder(List.of(p1, p2), supplier1);

        Product p3 = new Product(3, "Butter", 0.2, Category.DAIRY, null, 12.0);
        Product p4 = new Product(4, "Eggs", 0.6, Category.DAIRY, null, 7.0);
        po2 = new ProductOrder(List.of(p3, p4), supplier2);

        address = new Address("Koszykowa", "Warsaw", "00-000", "Poland");
        newAddress = new Address("Nowogrodzka", "Warsaw", "00-000", "Poland");

        invoice = new Invoice(PaymentMethod.CARD, 10, 123456789, "Emilia",
                "Koszykowa", "Warsaw", "00-000", "Poland");
    }

    @Test
    void testFullConstructor() {
        assertEquals(10, invoice.getID());
        assertEquals(123456789, invoice.getTaxIdentificationNumber());
        assertEquals("Emilia", invoice.getName());
        assertEquals(address, invoice.getAddress());
        assertEquals(0, invoice.getSum());
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
    void testAddSupplyHistoryAddsReverseConnection() {
        SupplyHistory sh = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        assertTrue(invoice.getSupplyHistoryList().contains(sh));
        assertEquals(invoice, sh.getInvoice());
    }

    @Test
    void testAddMultipleSupplyHistoryEntries() {
        SupplyHistory s1 = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        SupplyHistory s2 = new SupplyHistory(LocalDate.now().plusDays(1), SupplyStatus.ORDERED, invoice, po2);

        assertEquals(2, invoice.getSupplyHistoryList().size());
    }

    @Test
    void testCannotRemoveSupplyHistoryFromInvoice() {
        SupplyHistory sh = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        assertThrows(IllegalStateException.class, () -> invoice.removeSupplyHistory(sh));
    }

    @Test
    void testSumUpdatesWithSupplyHistory() {
        SupplyHistory sh1 = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        assertEquals(po1.getTotalSum(), invoice.getSum());

        SupplyHistory sh2 = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po2);
        assertEquals(po1.getTotalSum() + po2.getTotalSum(), invoice.getSum());
    }

    @Test
    void testDeliveredRequiresOrderedBefore() {
        new SupplyHistory(LocalDate.now().minusDays(2), SupplyStatus.ORDERED, invoice, po1);
        SupplyHistory sh2 = new SupplyHistory(LocalDate.now(), SupplyStatus.DELIVERED, invoice, po1);

        assertEquals(SupplyStatus.DELIVERED, sh2.getStatus());
    }

    @Test
    void testDeliveredWithoutOrderedThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new SupplyHistory(LocalDate.now(), SupplyStatus.DELIVERED, invoice, po1));
    }

    @Test
    void testDeliveredWithOrderedSameDayThrowsException() {
        new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        assertThrows(IllegalArgumentException.class, () ->
                new SupplyHistory(LocalDate.now(), SupplyStatus.DELIVERED, invoice, po1));
    }

    @Test
    void testExtentAdd() {
        Invoice invoice1 = new Invoice(PaymentMethod.CASH, 30, 555555555, "Emilia",
                "Nowogrodzka", "Warsaw", "00-000", "Poland");
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
        Invoice v = new Invoice(PaymentMethod.VOUCHER, 50, 888888888, "Emilia",
                "Koszykowa", "Warsaw", "00-000", "Poland");
        assertThrows(UnsupportedOperationException.class, () -> Invoice.getExtent().add(v));
    }

    @Test
    void testRemoveFromExtent() {
        Invoice invoiceToRemove =
                new Invoice(PaymentMethod.CARD, 40, 999888777, "Emilia",
                        "Koszykowa", "Warsaw", "00-000", "Poland");

        assertTrue(Invoice.getExtent().contains(invoiceToRemove));

        Invoice.removeFromExtent(invoiceToRemove);
        assertFalse(Invoice.getExtent().contains(invoiceToRemove));
    }

    @Test
    void testMultipleInvoices() {
        Invoice i1 = new Invoice(PaymentMethod.CASH, 100, 111111111, "Emilia",
                "Koszykowa", "Warsaw", "00-000", "Poland");
        Invoice i2 = new Invoice(PaymentMethod.CARD, 200, 222222222, "Emilia",
                "Nowogrodzka", "Warsaw", "00-000", "Poland");

        assertEquals(3, Invoice.getExtent().size());
        assertTrue(Invoice.getExtent().contains(invoice));
        assertTrue(Invoice.getExtent().contains(i1));
        assertTrue(Invoice.getExtent().contains(i2));
    }

    @Test
    void testClearExtentWorksProperly() throws Exception {
        Invoice i1 = new Invoice(PaymentMethod.CASH, 300, 333333333, "Emilia",
                "Koszykowa", "Warsaw", "00-000", "Poland");
        Invoice i2 = new Invoice(PaymentMethod.CARD, 400, 444444444, "Emilia",
                "Nowogrodzka", "Warsaw", "00-000", "Poland");

        assertEquals(3, Invoice.getExtent().size());
        assertTrue(Invoice.getExtent().contains(i1));
        assertTrue(Invoice.getExtent().contains(i2));

        Method clearInvoice = Invoice.class.getDeclaredMethod("clearExtent");
        clearInvoice.setAccessible(true);
        clearInvoice.invoke(null);

        assertEquals(0, Invoice.getExtent().size());
    }

    @Test
    void testSupplyHistoryAddedToInvoiceAutomatically() {
        SupplyHistory sh = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        assertEquals(1, invoice.getSupplyHistoryList().size());
        assertTrue(invoice.getSupplyHistoryList().contains(sh));
    }

    @Test
    void testSupplyHistoryAddsInvoiceReverse() {
        SupplyHistory sh = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        assertEquals(invoice, sh.getInvoice());
    }

    @Test
    void testSupplyHistoryAddedToProductOrderAutomatically() {
        SupplyHistory sh = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        assertEquals(1, po1.getSupplyHistoryList().size());
        assertTrue(po1.getSupplyHistoryList().contains(sh));
    }

    @Test
    void testSupplyHistoryAddsProductOrderReverse() {
        SupplyHistory sh = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        assertEquals(po1, sh.getProductOrder());
    }

    @Test
    void testMultipleSupplyHistoryObjectsConnectCorrectly() {
        SupplyHistory s1 = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        SupplyHistory s2 = new SupplyHistory(LocalDate.now().plusDays(1), SupplyStatus.ORDERED, invoice, po1);
        assertEquals(2, invoice.getSupplyHistoryList().size());
        assertEquals(2, po1.getSupplyHistoryList().size());
    }

    @Test
    void testSupplyHistoryCannotRemoveInvoice() {
        SupplyHistory sh = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        assertThrows(IllegalStateException.class, () -> sh.removeInvoice());
    }

    @Test
    void testSupplyHistoryCannotRemoveProductOrder() {
        SupplyHistory sh = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        assertThrows(IllegalStateException.class, () -> sh.removeProductOrder());
    }

    @Test
    void testDifferentProductOrdersMaintainSeparateHistories() {
        SupplyHistory s1 = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        SupplyHistory s2 = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po2);
        assertTrue(po1.getSupplyHistoryList().contains(s1));
        assertFalse(po1.getSupplyHistoryList().contains(s2));
        assertTrue(po2.getSupplyHistoryList().contains(s2));
        assertFalse(po2.getSupplyHistoryList().contains(s1));
    }

    @Test
    void testInvoiceSumOnlyItsOwnSupplyHistory() {
        SupplyHistory s1 = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        Invoice other = new Invoice(PaymentMethod.CARD, 77, 567890123, "Emilia",
                "Nowogrodzka", "Warsaw","00-000","Poland");
        SupplyHistory s2 = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, other, po2);
        assertEquals(po1.getTotalSum(), invoice.getSum());
        assertEquals(po2.getTotalSum(), other.getSum());
    }

    @Test
    void testSupplyHistoryBelongsToExactlyOneInvoice() {
        SupplyHistory sh = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, invoice, po1);
        assertEquals(invoice, sh.getInvoice());
        Invoice other = new Invoice(PaymentMethod.CARD, 88, 999555444, "Emilia",
                "Koszykowa","Warsaw","00-000","Poland");
        assertThrows(IllegalStateException.class, () ->
                new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, other, po1));
    }
}