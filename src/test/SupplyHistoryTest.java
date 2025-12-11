package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SupplyHistoryTest {

    private ProductOrder productOrder;
    private Invoice invoice;
    private SupplyHistory supplyHistory;
    private LocalDate today;
    private Supplier supplier;

    @BeforeEach
    void setup() throws Exception {

        Method clearProduct = Product.class.getDeclaredMethod("clearExtent");
        clearProduct.setAccessible(true);
        clearProduct.invoke(null);

        Method clearSupplier = Supplier.class.getDeclaredMethod("clearExtent");
        clearSupplier.setAccessible(true);
        clearSupplier.invoke(null);

        Method clearProductOrder = ProductOrder.class.getDeclaredMethod("clearExtent");
        clearProductOrder.setAccessible(true);
        clearProductOrder.invoke(null);

        Method clearInvoice = Invoice.class.getDeclaredMethod("clearExtent");
        clearInvoice.setAccessible(true);
        clearInvoice.invoke(null);

        Method clearSupplyHistory = SupplyHistory.class.getDeclaredMethod("clearExtent");
        clearSupplyHistory.setAccessible(true);
        clearSupplyHistory.invoke(null);

        supplier = new Supplier(
                "Emilia", "Dec", "111222333",
                "Koszykowa", "Warsaw", "00-000", "Poland",
                "emiliadec@gmail.com", "Company", Category.DAIRY, 10.0
        );

        Product p1 = new Product(1, "Milk", 1.0, Category.DAIRY, null, 10.0);
        Product p2 = new Product(2, "Bread", 0.5, Category.BREAD, null, 5.0);

        productOrder = new ProductOrder(new HashSet<>(Set.of(p1, p2)), supplier);

        invoice = new Invoice(PaymentMethod.CARD, 1, 123456789, "Emilia",
                "Koszykowa", "Warsaw", "00-000", "Poland");

        today = LocalDate.now().minusDays(2);

        supplyHistory = new SupplyHistory(today.plusDays(2), SupplyStatus.ORDERED, invoice, productOrder);
    }

    @Test
    void testConstructorValid() {
        assertEquals(today.plusDays(2), supplyHistory.getDate());
        assertEquals(SupplyStatus.ORDERED, supplyHistory.getStatus());
        assertEquals(invoice, supplyHistory.getInvoice());
        assertEquals(productOrder, supplyHistory.getProductOrder());
    }

    @Test
    void testSetDateNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> supplyHistory.setDate(null));
    }

    @Test
    void testSetDateFutureThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> supplyHistory.setDate(LocalDate.now().plusDays(5)));
    }

    @Test
    void testSetStatusNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> supplyHistory.setStatus(null));
    }

    @Test
    void testConstructorNullInvoiceThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new SupplyHistory(today, SupplyStatus.ORDERED, null, productOrder));
    }

    @Test
    void testConstructorNullProductOrderThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new SupplyHistory(today, SupplyStatus.ORDERED, invoice, null));
    }

    @Test
    void testConstructorNullStatusThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new SupplyHistory(today, null, invoice, productOrder));
    }

    @Test
    void testConstructorNullDateThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new SupplyHistory(null, SupplyStatus.ORDERED, invoice, productOrder));
    }

    @Test
    void testDeliveredWithoutPreviousOrderedThrowsException() throws Exception {

        Supplier sup = new Supplier(
                "Anna","Szyr","222333444",
                "Nowogrodzka","Warsaw","00-000","Poland",
                "annaszyr@gmail.com","Firm", Category.DAIRY, 5.0
        );

        Product p3 = new Product(3, "Butter", 0.3, Category.DAIRY, null, 8.0);
        Product p4 = new Product(4, "Cheese", 0.4, Category.DAIRY, null, 12.0);
        ProductOrder otherOrder = new ProductOrder(new HashSet<>(Set.of(p3, p4)), sup);

        Invoice otherInvoice = new Invoice(PaymentMethod.CARD, 2000, 98765422,
                "AnnaX", "NowogrodzkaX", "Warsaw", "00-00", "Poland");

        assertThrows(IllegalArgumentException.class,
                () -> new SupplyHistory(today, SupplyStatus.DELIVERED, otherInvoice, otherOrder));
    }

    @Test
    void testDeliveredBeforeOrderedThrowsException() {
        LocalDate orderedDate = today;
        LocalDate deliveredBefore = today.minusDays(1);

        new SupplyHistory(orderedDate, SupplyStatus.ORDERED, invoice, productOrder);

        assertThrows(IllegalArgumentException.class,
                () -> new SupplyHistory(deliveredBefore, SupplyStatus.DELIVERED, invoice, productOrder));
    }

    @Test
    void testAddExtent() {
        SupplyHistory sh = new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);
        assertTrue(SupplyHistory.getExtent().contains(sh));
    }

    @Test
    void testAddExtentWithNull() {
        assertThrows(IllegalArgumentException.class, () -> SupplyHistory.addExtent(null));
    }

    @Test
    void testGetExtentIsUnmodifiable() {
        SupplyHistory valid = new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);
        assertThrows(UnsupportedOperationException.class,
                () -> SupplyHistory.getExtent().add(valid));
    }

    @Test
    void testRemoveFromExtent() {
        SupplyHistory sh = new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);
        assertTrue(SupplyHistory.getExtent().contains(sh));

        SupplyHistory.removeFromExtent(sh);
        assertFalse(SupplyHistory.getExtent().contains(sh));
    }

    @Test
    void testClearExtentWorksProperly() throws Exception {
        new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);
        new SupplyHistory(today.minusDays(1), SupplyStatus.ORDERED, invoice, productOrder);

        assertEquals(3, SupplyHistory.getExtent().size());

        Method clear = SupplyHistory.class.getDeclaredMethod("clearExtent");
        clear.setAccessible(true);
        clear.invoke(null);

        assertEquals(0, SupplyHistory.getExtent().size());
    }

    @Test
    void testSupplyHistoryAutomaticallyAddedToInvoice() {
        SupplyHistory sh = new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);
        assertTrue(invoice.getSupplyHistorySet().contains(sh));
    }

    @Test
    void testSupplyHistoryAutomaticallyAddedToProductOrder() {
        SupplyHistory sh = new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);
        assertTrue(productOrder.getSupplyHistories().contains(sh));
    }

    @Test
    void testTwoInvoicesCanHaveSeparateSupplyHistoriesForSameProductOrder() {
        new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);

        Invoice other = new Invoice(PaymentMethod.CARD, 98, 123123123,
                "Anna", "Nowogrodzka", "Warsaw", "00-000", "Poland");

        assertDoesNotThrow(() ->
                new SupplyHistory(today, SupplyStatus.ORDERED, other, productOrder));
    }

    @Test
    void testTwoProductOrdersCanShareSameInvoiceInDifferentSupplyHistories() throws Exception {
        new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);

        Supplier sup = new Supplier(
                "Anna","Szyr","111222333",
                "Koszykowa","Warsaw","00-000","Poland",
                "annaszyr@gmail.com","Firm", Category.DAIRY, 9.0
        );

        Product p3 = new Product(3, "Butter", 0.3, Category.DAIRY, null, 8.0);
        Product p4 = new Product(4, "Cheese", 0.4, Category.DAIRY, null, 12.0);
        ProductOrder otherPO = new ProductOrder(new HashSet<>(Set.of(p3, p4)), sup);

        assertDoesNotThrow(() ->
                new SupplyHistory(today.plusDays(1), SupplyStatus.ORDERED, invoice, otherPO));
    }

    @Test
    void testSupplyHistoryCannotRemoveInvoice() {
        SupplyHistory sh = new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);
        assertThrows(IllegalStateException.class, sh::removeInvoice);
    }

    @Test
    void testSupplyHistoryCannotRemoveProductOrder() {
        SupplyHistory sh = new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);
        assertThrows(IllegalStateException.class, sh::removeProductOrder);
    }
}