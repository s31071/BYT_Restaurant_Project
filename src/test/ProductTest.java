package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;

    @BeforeEach
    void setup() throws Exception {

        Method clearProduct = Product.class.getDeclaredMethod("clearExtent");
        clearProduct.setAccessible(true);
        clearProduct.invoke(null);

        product = new Product(1L, "Milk", 1.0, Category.DAIRY, null, 10.0);
    }

    @Test
    void testConstructorValidWithExpiryDate() {
        LocalDate expiry = LocalDate.now().plusDays(7);
        Product p = new Product(2L, "Cheese", 1.0, Category.DAIRY, expiry, 5.0);
        assertEquals(2L, p.getID());
        assertEquals("Cheese", p.getName());
        assertEquals(1.0, p.getWeight());
        assertEquals(Category.DAIRY, p.getCategory());
        assertEquals(expiry, p.getExpiryDate());
        assertEquals(5.0, p.getPrice());
    }

    @Test
    void testConstructorValidWithoutExpiryDate() {
        assertNull(product.getExpiryDate());
    }

    @Test
    void testSetIDInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> product.setID(0L));
    }

    @Test
    void testSetNameInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> product.setName(""));
        assertThrows(IllegalArgumentException.class, () -> product.setName(null));
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
    }

    @Test
    void testSetCategoryInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> product.setCategory(null));
    }

    @Test
    void testSetExpiryDatePastThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> product.setExpiryDate(LocalDate.now().minusDays(1)));
    }

    @Test
    void testConstructorNullNameThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(1L, null, 1.0, Category.DAIRY, null, 10.0));
    }

    @Test
    void testConstructorNullCategoryThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(1L, "Milk", 1.0, null, null, 10.0));
    }

    @Test
    void testConstructorZeroWeightThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(1L, "Milk", 0.0, Category.DAIRY, null, 10.0));
    }

    @Test
    void testConstructorNegativeWeightThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(1L, "Milk", -5.0, Category.DAIRY, null, 10.0));
    }

    @Test
    void testSetPriceValid() {
        product.setPrice(10.0);
        assertEquals(10.0, product.getPrice());
    }

    @Test
    void testSetPriceZeroThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> product.setPrice(0.0));
    }

    @Test
    void testSetPriceNegativeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> product.setPrice(-5.0));
    }

    @Test
    void testConstructorPriceValid() {
        Product p = new Product(10L, "Juice", 1.5, Category.BEVERAGES, null, 12.5);
        assertEquals(12.5, p.getPrice());
    }

    @Test
    void testConstructorPriceZeroThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(10L, "Juice", 1.5, Category.BEVERAGES, null, 0.0));
    }

    @Test
    void testConstructorPriceNegativeThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(10L, "Juice", 1.5, Category.BEVERAGES, null, -3.0));
    }

    @Test
    void testAddExtentAddsProduct() {
        assertEquals(1, Product.getExtent().size());
        assertTrue(Product.getExtent().contains(product));
    }

    @Test
    void testAddExtentWithNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Product.addExtent(null));
    }

    @Test
    void testAddExtentDuplicateThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Product.addExtent(product));
    }

    @Test
    void testGetExtentIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Product.getExtent().add(product);
        });
    }

    @Test
    void testRemoveFromExtentRemovesProduct() {
        assertTrue(Product.getExtent().contains(product));
        Product.removeFromExtent(product);
        assertFalse(Product.getExtent().contains(product));
    }

    @Test
    void testRemoveMethodRemovesFromExtent() {
        assertTrue(Product.getExtent().contains(product));
        product.remove();
        assertFalse(Product.getExtent().contains(product));
    }

    @Test
    void testClearExtentEmptiesExtent() throws Exception {
        Product p2 = new Product(2L, "Cheese", 1.0, Category.DAIRY, null, 5.0);
        assertEquals(2, Product.getExtent().size());

        Method clearProduct = Product.class.getDeclaredMethod("clearExtent");
        clearProduct.setAccessible(true);
        clearProduct.invoke(null);

        assertEquals(0, Product.getExtent().size());
    }

    @Test
    void testConstructorWithoutPriceWithExpiryThrowsException() {
        LocalDate expiry = LocalDate.now().plusDays(3);
        assertThrows(IllegalArgumentException.class,
                () -> new Product(3L, "Rice", 1.0, Category.DAIRY, expiry));
    }

    @Test
    void testConstructorWithoutPriceAndExpiryThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(4L, "Rice", 1.0, Category.DAIRY));
    }
}