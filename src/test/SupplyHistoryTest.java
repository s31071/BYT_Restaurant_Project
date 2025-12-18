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

        Method clearPerson = Person.class.getDeclaredMethod("clearExtent");
        clearPerson.setAccessible(true);
        clearPerson.invoke(null);

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

        Person pSupplier = new Person(
                "Emilia", "Dec", "111222333",
                "Koszykowa", "Warsaw", "00-000", "Poland",
                "emiliadec@gmail.com"
        );

        supplier = new Supplier(
                pSupplier, "Company", Category.DAIRY, 10.0
        );

        Product p1 = new Product(1, "Milk", 1.0, Category.DAIRY, null, 10.0);
        Product p2 = new Product(2, "Bread", 0.5, Category.BREAD, null, 5.0);

        productOrder = new ProductOrder(new HashSet<>(Set.of(p1, p2)), supplier);

        invoice = new Invoice(
                PaymentMethod.CARD, 1, 123456789, "Emilia",
                "Koszykowa", "Warsaw", "00-000", "Poland"
        );

        today = LocalDate.now().minusDays(2);

        supplyHistory = new SupplyHistory(
                today.plusDays(2),
                SupplyStatus.ORDERED,
                invoice,
                productOrder
        );
    }

    @Test
    void testConstructorValid() {
        assertEquals(today.plusDays(2), supplyHistory.getDate());
        assertEquals(SupplyStatus.ORDERED, supplyHistory.getStatus());
        assertEquals(invoice, supplyHistory.getInvoice());
        assertEquals(productOrder, supplyHistory.getProductOrder());
    }

    @Test
    void testDeliveredWithoutPreviousOrderedThrowsException() throws Exception {

        Person pSup = new Person(
                "Anna","Szyr","222333444",
                "Nowogrodzka","Warsaw","00-000","Poland",
                "annaszyr@gmail.com"
        );

        Supplier sup = new Supplier(
                pSup, "Firm", Category.DAIRY, 5.0
        );

        Product p3 = new Product(3, "Butter", 0.3, Category.DAIRY, null, 8.0);
        Product p4 = new Product(4, "Cheese", 0.4, Category.DAIRY, null, 12.0);

        ProductOrder otherOrder =
                new ProductOrder(new HashSet<>(Set.of(p3, p4)), sup);

        Invoice otherInvoice = new Invoice(
                PaymentMethod.CARD, 2000, 98765422,
                "AnnaX", "NowogrodzkaX", "Warsaw", "00-00", "Poland"
        );

        assertThrows(IllegalArgumentException.class,
                () -> new SupplyHistory(today, SupplyStatus.DELIVERED, otherInvoice, otherOrder));
    }

    @Test
    void testTwoProductOrdersCanShareSameInvoiceInDifferentSupplyHistories() throws Exception {

        new SupplyHistory(today, SupplyStatus.ORDERED, invoice, productOrder);

        Person pSup = new Person(
                "Anna","Szyr","111222333",
                "Koszykowa","Warsaw","00-000","Poland",
                "annaszyr@gmail.com"
        );

        Supplier sup = new Supplier(
                pSup, "Firm", Category.DAIRY, 9.0
        );

        Product p3 = new Product(3, "Butter", 0.3, Category.DAIRY, null, 8.0);
        Product p4 = new Product(4, "Cheese", 0.4, Category.DAIRY, null, 12.0);

        ProductOrder otherPO =
                new ProductOrder(new HashSet<>(Set.of(p3, p4)), sup);

        assertDoesNotThrow(() ->
                new SupplyHistory(today.plusDays(1), SupplyStatus.ORDERED, invoice, otherPO));
    }
}