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

    private ProductOrder productOrder;
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

        Method clearInvoice = Invoice.class.getDeclaredMethod("clearExtent");
        clearInvoice.setAccessible(true);
        clearInvoice.invoke(null);

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

        productOrder = new ProductOrder(List.of(p1, p2), supplier1);
    }

    @Test
    void testConstructorInitialValues() {
        assertEquals(List.of(p1, p2), productOrder.getProducts());
        assertEquals(supplier1, productOrder.getSupplier());
        assertEquals(1.5, productOrder.getTotalWeight());
        assertEquals(5.5, productOrder.getTotalSum());
    }

    @Test
    void testTotalsRecomputedAfterSetProducts() {
        Product p3 = new Product(3, "Butter", 0.2, Category.DAIRY, null, 5.00);
        Product p4 = new Product(4, "Eggs", 0.6, Category.DAIRY, null, 4.00);

        productOrder.setProducts(List.of(p3, p4));

        assertEquals(0.8, productOrder.getTotalWeight());
        assertEquals(9.0, productOrder.getTotalSum());
    }

    @Test
    void testRoundingForTotals() {
        Product p3 = new Product(3, "Rice", 0.33333, Category.DAIRY, null, 1.9999);
        Product p4 = new Product(4, "Beans", 0.66666, Category.DAIRY, null, 3.1111);

        productOrder.setProducts(List.of(p3, p4));

        assertEquals(1.0, productOrder.getTotalWeight());
        assertEquals(5.11, productOrder.getTotalSum());
    }

    @Test
    void testSetProductsNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> productOrder.setProducts(null));
    }

    @Test
    void testSetProductsEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> productOrder.setProducts(List.of()));
    }

    @Test
    void testSetProductsContainsNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> productOrder.setProducts(Arrays.asList(p1, null)));
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
        assertThrows(IllegalArgumentException.class, () -> ProductOrder.addExtent(productOrder));
    }

    @Test
    void testGetExtentIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () ->
                ProductOrder.getExtent().add(productOrder));
    }

    @Test
    void testRemoveFromExtent() {
        assertTrue(ProductOrder.getExtent().contains(productOrder));
        ProductOrder.removeFromExtent(productOrder);
        assertFalse(ProductOrder.getExtent().contains(productOrder));
    }

    @Test
    void testMultipleOrdersInExtent() throws Exception {
        ProductOrder o1 = new ProductOrder(List.of(p1), supplier1);
        ProductOrder o2 = new ProductOrder(List.of(p2), supplier1);
        assertEquals(3, ProductOrder.getExtent().size());
        assertTrue(ProductOrder.getExtent().contains(productOrder));
        assertTrue(ProductOrder.getExtent().contains(o1));
        assertTrue(ProductOrder.getExtent().contains(o2));
    }

    @Test
    void testTotalsRecomputeAfterEachCall() {
        Product p3 = new Product(3, "Chocolate", 2.0, Category.DAIRY, null, 6.0);

        productOrder.setProducts(List.of(p1));
        assertEquals(1.0, productOrder.getTotalWeight());
        assertEquals(3.50, productOrder.getTotalSum());

        productOrder.setProducts(List.of(p1, p3));
        assertEquals(3.0, productOrder.getTotalWeight());
        assertEquals(9.50, productOrder.getTotalSum());
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
        assertThrows(Exception.class, () -> productOrder.addSupplier(supplier2));

        assertEquals(supplier1, productOrder.getSupplier());
        assertTrue(supplier1.getProductOrders().contains(productOrder));
    }

    @Test
    void testSupplierNullThrows() {
        assertThrows(Exception.class, () -> productOrder.addSupplier(null));
    }

    @Test
    void testSupplierCannotBeRemoved() throws Exception {
        productOrder.removeSupplier(supplier1);

        assertNull(productOrder.getSupplier());
        assertFalse(supplier1.getProductOrders().contains(productOrder));
    }

    @Test
    void testSupplierUpdatesReverse() throws Exception {
        assertThrows(Exception.class, () -> productOrder.addSupplier(supplier2));

        assertTrue(supplier1.getProductOrders().contains(productOrder));
        assertFalse(supplier2.getProductOrders().contains(productOrder));
    }

    @Test
    void testAddSupplyHistoryCreatesReverse() {
        SupplyHistory sh = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED,
                new Invoice(PaymentMethod.CASH, 1, 111, "Emilia",
                        "Koszykowa", "Warsaw", "00-000", "Poland"),
                productOrder);
        assertTrue(productOrder.getSupplyHistoryList().contains(sh));
        assertEquals(productOrder, sh.getProductOrder());
    }

    @Test
    void testRemoveSupplyHistoryNotAllowed() {
        SupplyHistory sh = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED,
                new Invoice(PaymentMethod.CASH, 2, 222, "Anna",
                        "Nowogrodzka", "Warsaw", "00-000", "Poland"),
                productOrder);
        assertThrows(IllegalStateException.class, () -> productOrder.removeSupplyHistory(sh));
    }

    @Test
    void testMultipleSupplyHistoriesStored() {
        Invoice inv = new Invoice(PaymentMethod.CASH, 3, 333, "Emilia",
                "Koszykowa","Warsaw","00-000","Poland");
        SupplyHistory s1 = new SupplyHistory(LocalDate.now(), SupplyStatus.ORDERED, inv, productOrder);
        SupplyHistory s2 = new SupplyHistory(LocalDate.now().minusDays(1), SupplyStatus.ORDERED, inv, productOrder);

        assertEquals(2, productOrder.getSupplyHistoryList().size());
    }

    @Test
    void testProductOrderConnectedToProductsBothDirections() {
        Product p3 = new Product(5, "Cheese", 0.4, Category.DAIRY, null, 8.0);
        productOrder.addProduct(p3);
        assertTrue(productOrder.getProducts().contains(p3));
        assertTrue(p3.getProductOrders().contains(productOrder));
    }

    @Test
    void testRemoveProductMinimumLimit() {
        assertThrows(IllegalStateException.class, () -> {
            productOrder.removeProduct(p1);
            productOrder.removeProduct(p2);
        });
    }

    @Test
    void testRemoveProductUpdatesReverse() throws Exception {
        ProductOrder anotherOrder = new ProductOrder(List.of(p2), supplier1);

        productOrder.removeProduct(p2);

        assertFalse(productOrder.getProducts().contains(p2));
        assertFalse(p2.getProductOrders().contains(productOrder));
        assertTrue(p2.getProductOrders().contains(anotherOrder));
    }
}