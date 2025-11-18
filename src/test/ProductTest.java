package test;

import classes.Category;
import classes.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;

    @BeforeEach
    void setup() throws Exception {
        Field extentField = Product.class.getDeclaredField("extent");
        extentField.setAccessible(true);
        ((List<?>) extentField.get(null)).clear();

        product = new Product(1L, "Milk", 1.0, Category.DAIRY);
    }

    @Test
    void testConstructorValidWithExpiryDate() {
        LocalDate expiry = LocalDate.now().plusDays(7);
        Product p = new Product(2L, "Cheese", 1.0, Category.DAIRY, expiry);
        assertEquals(2L, p.getID());
        assertEquals("Cheese", p.getName());
        assertEquals(1.0, p.getWeight());
        assertEquals(Category.DAIRY, p.getCategory());
        assertEquals(expiry, p.getExpiryDate());
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
        assertThrows(IllegalArgumentException.class, () -> product.setWeight(null));
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
    void testExtentAddsAutomatically() {
        new Product(5L, "Kefir", 2.0, Category.DAIRY);
        new Product(6L, "Ham", 3.0, Category.MEAT);
        assertEquals(3, Product.getExtent().size());
    }

    @Test
    void testRemoveRemovesFromExtent() {
        product.remove();
        assertFalse(Product.getExtent().contains(product));
    }

    @Test
    void testConstructorNullNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Product(1L, null, 1.0, Category.DAIRY));
    }

    @Test
    void testConstructorNullCategoryThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Product(1L, "Milk", 1.0, null));
    }

    @Test
    void testConstructorZeroWeightThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Product(1L, "Milk", 0.0, Category.DAIRY));
    }

    @Test
    void testConstructorNegativeWeightThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Product(1L, "Milk", -5.0, Category.DAIRY));
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
        assertThrows(IllegalArgumentException.class, () -> new Product(10L, "Juice", 1.5, Category.BEVERAGES, null, 0.0));
    }

    @Test
    void testConstructorPriceNegativeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Product(10L, "Juice", 1.5, Category.BEVERAGES, null, -3.0));
    }
}