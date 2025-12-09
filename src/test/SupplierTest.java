package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {
    Supplier s;

    @BeforeEach
    void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method clearMethod = Supplier.class.getDeclaredMethod("clearExtent");
        clearMethod.setAccessible(true);
        clearMethod.invoke(null);
        s = new Supplier("Adam", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "adam@example.com",
                "BestMeat", Category.MEAT, 120.0);
    }

    @Test
    void testConstructor() {
        assertEquals("Adam", s.getName());
        assertEquals("BestMeat", s.getCompanyName());
        assertEquals(Category.MEAT, s.getCategory());
    }

    @Test
    void shouldThrowExceptionForEmptyCompanyName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("Adam", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "adam@example.com",
                        "", Category.MEAT, 120.0));
    }

    @Test
    void shouldThrowExceptionForNullCompanyName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("Adam", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "adam@example.com",
                        null, Category.MEAT, 120.0));
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("Adam", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "adam#example.com",
                        "BestMeat", Category.MEAT, 120.0));
    }

    @Test
    void shouldThrowExceptionForNullCategory() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("Adam", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500",
                        "Poland", "adam@example.com", "BestMeat", null, 120.0));
    }

    @Test
    void shouldThrowExceptionForNegativeDeliveryCost() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("Adam", "Nowak", "123456789", "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "adam@example.com", "BestMeat", Category.MEAT, -1.0));
    }

    @Test
    void testSetDeliveryCostZeroValid() {
        s.setDeliveryCost(0.0);
        assertEquals(0.0, s.getDeliveryCost());
    }

    @Test
    void testSetCompanyNameValid() {
        s.setCompanyName("FreshFood");
        assertEquals("FreshFood", s.getCompanyName());
    }

    @Test
    void testSetCompanyNameBlankThrows() {
        assertThrows(IllegalArgumentException.class, () -> s.setCompanyName(" "));
    }

    @Test
    void testSetCategoryValid() {
        s.setCategory(Category.VEGETABLES);
        assertEquals(Category.VEGETABLES, s.getCategory());
    }

    @Test
    void testSetDeliveryCostValid() {
        s.setDeliveryCost(50.0);
        assertEquals(50.0, s.getDeliveryCost());
    }

    @Test
    void testAddExtentDuplicateSupplierThrowsException() throws Exception {
        Method method = Supplier.class.getDeclaredMethod("addExtent", Supplier.class);
        method.setAccessible(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Supplier(
                        "Adam", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "adam@example.com",
                        "BestMeat", Category.MEAT, 120.0
                )
        );

        assertEquals("Such supplier is already in data base", ex.getMessage());
    }

    @Test
    void testExtentContainsOnlyOneSupplierAfterDuplicateAttempt() throws Exception {
        Method addMethod = Supplier.class.getDeclaredMethod("addExtent", Supplier.class);
        addMethod.setAccessible(true);

        Method getExtentMethod = Supplier.class.getDeclaredMethod("getExtent");
        getExtentMethod.setAccessible(true);

        try {
            Supplier duplicate = new Supplier("Adam", "Nowak", "123456789", "Markowskiego", "Piaseczno","05-500", "Poland", "adam@example.com",
                    "BestMeat", Category.MEAT, 120.0);
        } catch (IllegalArgumentException ignored) {}

        var extent = (java.util.List<Supplier>) getExtentMethod.invoke(null);
        assertEquals(1, extent.size());
        assertEquals(s, extent.get(0));
    }

    @Test
    void testRemoveFromExtent() throws Exception {
        Supplier s2 = new Supplier(
                "Eva", "Nowak", "555444333", "Koszykowa", "Warszawa", "00-001", "Poland",
                "s31431@pjwstk.edu.pl", "GreenVeg", Category.VEGETABLES, 75.0
        );

        Method getExtent = Supplier.class.getDeclaredMethod("getExtent");
        getExtent.setAccessible(true);
        var extent = (java.util.List<Supplier>) getExtent.invoke(null);

        assertEquals(2, extent.size());
        assertTrue(extent.contains(s2));

        Supplier.removeFromExtent(s2);

        extent = (java.util.List<Supplier>) getExtent.invoke(null);
        assertEquals(1, extent.size());
        assertFalse(extent.contains(s2));
        assertTrue(extent.contains(s));
    }

    @Test
    void testClearExtent() throws Exception {
        Supplier s2 = new Supplier(
                "Eva", "Nowak", "555444333", "Koszykowa", "Warszawa", "00-001", "Poland",
                "eva@example.com", "GreenVeg", Category.VEGETABLES, 75.0
        );

        Method getExtent = Supplier.class.getDeclaredMethod("getExtent");
        getExtent.setAccessible(true);
        var extent = (java.util.List<Supplier>) getExtent.invoke(null);

        assertEquals(2, extent.size());
        assertTrue(extent.contains(s));
        assertTrue(extent.contains(s2));

        Method clearMethod = Supplier.class.getDeclaredMethod("clearExtent");
        clearMethod.setAccessible(true);
        clearMethod.invoke(null);

        extent = (java.util.List<Supplier>) getExtent.invoke(null);
        assertEquals(0, extent.size());
    }

    @Test
    void testAddOrderedProductNullThrows() {
        assertThrows(Exception.class, () -> s.addOrderedProduct(null));
    }

    @Test
    void testRemoveOrderedProductNotAssignedThrows() throws Exception {
        Supplier s2 = new Supplier(
                "Eva", "Nowak", "555444333", "Koszykowa", "Warszawa", "00-001", "Poland",
                "eva@example.com", "GreenVeg", Category.VEGETABLES, 75.0
        );
        Product p1 = new Product(3, "Turkey", 3.0, Category.MEAT, LocalDate.now().plusDays(7), 70.0);

        ProductOrder productOrder = new ProductOrder(new HashSet<>(Set.of(p1)), s2);

        assertThrows(Exception.class, () -> s.removeOrderedProduct(productOrder));
    }

    @Test
    void testRemoveOrderedProductRemovesFromExtent() throws Exception {
        Method clearPOExtent = ProductOrder.class.getDeclaredMethod("clearExtent");
        clearPOExtent.setAccessible(true);
        clearPOExtent.invoke(null);

        Product p1 = new Product(100, "Bread", 1.0, Category.MEAT, LocalDate.now().plusDays(2), 15.0);

        ProductOrder po = new ProductOrder(new HashSet<>(Set.of(p1)), s);

        assertTrue(s.getProductOrders().contains(po));
        assertTrue(ProductOrder.getExtent().contains(po));

        s.removeOrderedProduct(po);

        assertFalse(s.getProductOrders().contains(po));
        assertFalse(ProductOrder.getExtent().contains(po));
    }
}