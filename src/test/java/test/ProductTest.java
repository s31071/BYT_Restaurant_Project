package test.java.test;

import classes.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;

    @BeforeEach
    void setup() {
        Product.getProductList().clear();
        product = new Product(1, "Milk", 1.0, "Dairy");
    }

    @Test
    void testConstructorValidWithExpiryDate() {
        Product p = new Product(2, "Cheese", 1.0, "Dairy", LocalDate.now().plusDays(7));
        assertEquals(2, p.getID());
        assertEquals("Cheese", p.getName());
        assertEquals(1.0, p.getWeight());
        assertEquals("Dairy", p.getCategory());
        assertEquals(LocalDate.now().plusDays(7), p.getExpiryDate());
    }

    @Test
    void testConstructorValidWithoutExpiryDate() {
        assertNull(product.getExpiryDate());
    }

    @Test
    void testSetIDInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> product.setID(0));
    }

    @Test
    void testSetNameInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> product.setName(""));
    }

    @Test
    void testSetWeightValid() {
        product.setWeight(2.0);
        assertEquals(2.0, product.getWeight());
    }

    @Test
    void testSetWeightInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> product.setWeight(0.0));
        assertThrows(IllegalArgumentException.class, () -> product.setWeight(-1.0));
        assertThrows(IllegalArgumentException.class, () -> product.setWeight(null));
    }

    @Test
    void testSetCategoryInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> product.setCategory(""));
        assertThrows(IllegalArgumentException.class, () -> product.setCategory(null));
    }

    @Test
    void testSetExpiryDatePastThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> product.setExpiryDate(LocalDate.now().minusDays(1)));
    }

    @Test
    void testExtentAddsAutomatically() {
        new Product(5, "Chocolate", 2.0, "Sweets");
        new Product(6, "Ham", 3.0, "Meat");
        assertEquals(3, Product.getProductList().size());
    }

    @Test
    void testRemoveRemovesFromExtent() {
        product.remove();
        assertFalse(Product.getProductList().contains(product));
    }
    @Test
    void testConstructorNullNameThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(1, null, 1.0, "Dairy"));
    }

    @Test
    void testConstructorNullCategoryThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(1, "Milk", 1.0, null));
    }

    @Test
    void testConstructorZeroWeightThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(1, "Milk", 0.0, "Dairy"));
    }

    @Test
    void testConstructorNegativeWeightThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(1, "Milk", -5.0, "Dairy"));
    }
}