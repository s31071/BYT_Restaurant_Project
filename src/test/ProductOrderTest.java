package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductOrderTest {

    private ProductOrder order;
    private Product p1;
    private Product p2;
    private Supplier supplier1;
    private Supplier supplier2;

    @BeforeEach
    void setup() throws Exception {

        Method clearProduct = Product.class.getDeclaredMethod("clearExtent");
        clearProduct.setAccessible(true);
        clearProduct.invoke(null);

        Method clearProductOrder = ProductOrder.class.getDeclaredMethod("clearExtent");
        clearProductOrder.setAccessible(true);
        clearProductOrder.invoke(null);

        Method clearSupplier = Supplier.class.getDeclaredMethod("clearExtent");
        clearSupplier.setAccessible(true);
        clearSupplier.invoke(null);

        supplier1 = new Supplier(
                "Emilia", "Dec", "111222333",
                "Koszykowa", "Warsaw", "00-000", "Poland",
                "emiliadec@gmail.com", "CompanyA", Category.DAIRY, 5.0
        );

        supplier2 = new Supplier(
                "Anna", "Szyr", "444555666",
                "Nowogrodzka", "Warsaw", "00-000", "Poland",
                "annaszyr@gmail.com", "CompanyB", Category.BREAD, 8.0
        );

        p1 = new Product(1, "Milk", 1.0, Category.DAIRY, null, 3.50);
        p2 = new Product(2, "Bread", 0.5, Category.BREAD, null, 2.00);

        order = new ProductOrder(List.of(p1, p2), supplier1);
    }

    @Test
    void testConstructorInitialValues() {
        assertEquals(List.of(p1, p2), order.getProducts());
        assertEquals(supplier1, order.getSupplier());
        assertEquals(1.5, order.getTotalWeight());
        assertEquals(5.5, order.getTotalSum());
    }

    @Test
    void testTotalsRecomputedAfterSetProducts() {
        Product p3 = new Product(3, "Butter", 0.2, Category.DAIRY, null, 5.00);
        Product p4 = new Product(4, "Eggs", 0.6, Category.DAIRY, null, 4.00);

        order.setProducts(List.of(p3, p4));

        assertEquals(0.8, order.getTotalWeight());
        assertEquals(9.0, order.getTotalSum());
    }

    @Test
    void testRoundingForTotals() {
        Product p3 = new Product(3, "Rice", 0.33333, Category.DAIRY, null, 1.9999);
        Product p4 = new Product(4, "Beans", 0.66666, Category.DAIRY, null, 3.1111);

        order.setProducts(List.of(p3, p4));

        assertEquals(1.0, order.getTotalWeight());
        assertEquals(5.11, order.getTotalSum());
    }

    @Test
    void testSetProductsNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> order.setProducts(null));
    }

    @Test
    void testSetProductsEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> order.setProducts(List.of()));
    }

    @Test
    void testSetProductsContainsNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> order.setProducts(Arrays.asList(p1, null)));
    }

    @Test
    void testAddExtent() throws Exception {
        ProductOrder order2 = new ProductOrder(List.of(p1), supplier1);
        assertEquals(2, ProductOrder.getExtent().size());
        assertTrue(ProductOrder.getExtent().contains(order2));
    }

    @Test
    void testAddExtentWithNull() {
        assertThrows(IllegalArgumentException.class, () -> ProductOrder.addExtent(null));
    }

    @Test
    void testAddExtentDuplicateThrows() {
        assertThrows(IllegalArgumentException.class, () -> ProductOrder.addExtent(order));
    }

    @Test
    void testGetExtentIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () ->
                ProductOrder.getExtent().add(order));
    }

    @Test
    void testRemoveFromExtent() {
        assertTrue(ProductOrder.getExtent().contains(order));
        ProductOrder.removeFromExtent(order);
        assertFalse(ProductOrder.getExtent().contains(order));
    }

    @Test
    void testMultipleOrdersInExtent() throws Exception {
        ProductOrder o1 = new ProductOrder(List.of(p1), supplier1);
        ProductOrder o2 = new ProductOrder(List.of(p2), supplier1);
        assertEquals(3, ProductOrder.getExtent().size());
        assertTrue(ProductOrder.getExtent().contains(order));
        assertTrue(ProductOrder.getExtent().contains(o1));
        assertTrue(ProductOrder.getExtent().contains(o2));
    }

    @Test
    void testTotalsRecomputeAfterEachCall() {
        Product p3 = new Product(3, "Chocolate", 2.0, Category.DAIRY, null, 6.0);

        order.setProducts(List.of(p1));
        assertEquals(1.0, order.getTotalWeight());
        assertEquals(3.50, order.getTotalSum());

        order.setProducts(List.of(p1, p3));
        assertEquals(3.0, order.getTotalWeight());
        assertEquals(9.50, order.getTotalSum());
    }

    @Test
    void testClearExtentWorksProperly() throws Exception {
        ProductOrder o1 = new ProductOrder(List.of(p1), supplier1);
        ProductOrder o2 = new ProductOrder(List.of(p2), supplier1);

        assertEquals(3, ProductOrder.getExtent().size());

        Method clearProductOrder = ProductOrder.class.getDeclaredMethod("clearExtent");
        clearProductOrder.setAccessible(true);
        clearProductOrder.invoke(null);

        assertEquals(0, ProductOrder.getExtent().size());
    }

    @Test
    void testSupplierSetValid() throws Exception {
        order.setSupplier(supplier2);
        assertEquals(supplier2, order.getSupplier());
        assertTrue(supplier2.getProductOrders().contains(order));
    }

    @Test
    void testSupplierNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> order.setSupplier(null));
    }

    @Test
    void testSupplierCannotBeRemoved() {
        assertThrows(IllegalStateException.class, () -> order.removeSupplier());
    }

    @Test
    void testSupplierUpdatesReverse() throws Exception {
        order.setSupplier(supplier2);
        assertFalse(supplier1.getProductOrders().contains(order));
        assertTrue(supplier2.getProductOrders().contains(order));
    }

    @Test
    void testAddSupplyHistoryCreatesReverse() {
        SupplyHistory sh = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED,
                new Invoice(PaymentMethod.CASH, 1, 111, "Emilia",
                        "Koszykowa", "Warsaw", "00-000", "Poland"),
                order);
        assertTrue(order.getSupplyHistoryList().contains(sh));
        assertEquals(order, sh.getProductOrder());
    }

    @Test
    void testRemoveSupplyHistoryNotAllowed() {
        SupplyHistory sh = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED,
                new Invoice(PaymentMethod.CASH, 2, 222, "Anna",
                        "Nowogrodzka", "Warsaw", "00-000", "Poland"),
                order);
        assertThrows(IllegalStateException.class, () -> order.removeSupplyHistory(sh));
    }

    @Test
    void testMultipleSupplyHistoriesStored() {
        Invoice inv = new Invoice(PaymentMethod.CASH, 3, 333, "Emilia",
                "Koszykowa","Warsaw","00-000","Poland");
        SupplyHistory s1 = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, inv, order);
        SupplyHistory s2 = new SupplyHistory(LocalDate.now().plusDays(1), SupplyStatus.ORDERED, inv, order);

        assertEquals(2, order.getSupplyHistoryList().size());
    }

    @Test
    void testProductOrderConnectedToProductsBothDirections() {
        Product p3 = new Product(5, "Cheese", 0.4, Category.DAIRY, null, 8.0);
        order.addProduct(p3);
        assertTrue(order.getProducts().contains(p3));
        assertTrue(p3.getProductOrders().contains(order));
    }

    @Test
    void testRemoveProductMinimumLimit() {
        assertThrows(IllegalStateException.class, () -> {
            order.removeProduct(p1);
            order.removeProduct(p2);
        });
    }

    @Test
    void testRemoveProductUpdatesReverse() {
        order.removeProduct(p2);
        assertFalse(order.getProducts().contains(p2));
        assertFalse(p2.getProductOrders().contains(order));
    }
}