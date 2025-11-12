package test.java.test;

import classes.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @BeforeEach
    void resetExtent() {
        Product.getProductList().clear();
    }

    @Test
    void testConstructor_validWithExpiryDate() {
        Product p = new Product(1, "Milk", 1.0, "Dairy", LocalDate.now().plusDays(7));
        assertEquals(1, p.getID());
        assertEquals("Milk", p.getName());
        assertEquals(1.0, p.getWeight());
        assertEquals("Dairy", p.getCategory());
        assertEquals(LocalDate.now().plusDays(7), p.getExpiryDate());
    }

    @Test
    void testConstructor_validWithoutExpiryDate() {
        Product p = new Product(1, "Milk", 1.0, "Dairy");
        assertNull(p.getExpiryDate());
    }

    @Test
    void testSetID_invalid_throwsException() {
        Product p = new Product(1, "Milk", 1.0, "Dairy");
        assertThrows(IllegalArgumentException.class, () -> p.setID(0));
    }

    @Test
    void testSetName_invalid_throwsException() {
        Product p = new Product(1, "Milk", 1.0, "Dairy");
        assertThrows(IllegalArgumentException.class, () -> p.setName(""));
    }

    @Test
    void testSetWeight_valid() {
        Product p = new Product(1, "Milk", 1.0, "Dairy");
        p.setWeight(2.0);
        assertEquals(2.0, p.getWeight());
    }

    @Test
    void testSetWeight_invalid_throwsException() {
        Product p = new Product(1, "Milk", 1.0, "Dairy");
        assertThrows(IllegalArgumentException.class, () -> p.setWeight(0.0));
        assertThrows(IllegalArgumentException.class, () -> p.setWeight(-1.0));
        assertThrows(IllegalArgumentException.class, () -> p.setWeight(null));
    }

    @Test
    void testSetCategory_invalid_throwsException() {
        Product p = new Product(1, "Milk", 1.0, "Dairy");
        assertThrows(IllegalArgumentException.class, () -> p.setCategory(""));
        assertThrows(IllegalArgumentException.class, () -> p.setCategory(null));
    }

    @Test
    void testSetExpiryDate_past_throwsException() {
        Product p = new Product(1, "Milk", 1.0, "Dairy");
        assertThrows(IllegalArgumentException.class, () -> p.setExpiryDate(LocalDate.now().minusDays(1)));
    }

    @Test
    void testExtent_addsAutomatically() {
        new Product(1, "Milk", 1.0, "Dairy");
        new Product(2, "Milk", 1.0, "Dairy");
        assertEquals(2, Product.getProductList().size());
    }

    @Test
    void testRemove_removesFromExtent() {
        Product p = new Product(1, "Milk", 1.0, "Dairy");
        p.remove();
        assertFalse(Product.getProductList().contains(p));
    }
}