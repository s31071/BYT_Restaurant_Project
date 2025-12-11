package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;
    private Product product1;
    private Product product2;
    private Dish dish1;
    private Dish dish2;

    @BeforeEach
    void setup() throws Exception {

        Method clearProduct = Product.class.getDeclaredMethod("clearExtent");
        clearProduct.setAccessible(true);
        clearProduct.invoke(null);

        Method clearDish = Dish.class.getDeclaredMethod("clearExtent");
        clearDish.setAccessible(true);
        clearDish.invoke(null);

        product = new Product(1L, "Milk", 1.0, Category.DAIRY, null, 10.0);
        product1 = new Product(2L, "Tomato", 0.5, Category.VEGETABLES, LocalDate.now().plusDays(10), 2.50);
        product2 = new Product(3L, "Cheese", 0.3, Category.DAIRY, LocalDate.now().plusDays(30), 5.00);

        List<Integer> reviews1 = new ArrayList<>();
        reviews1.add(4);
        reviews1.add(5);

        List<Integer> reviews2 = new ArrayList<>();
        reviews2.add(3);
        reviews2.add(4);

        dish1 = new Dish("Pasta Carbonara", 12.99, reviews1);
        dish2 = new Dish("Salad", 8.50, reviews2);
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
        assertEquals(3, Product.getExtent().size());
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
        product.removeFromExtent(product);
        assertFalse(Product.getExtent().contains(product));
    }

    @Test
    void testClearExtentWorksProperly() throws Exception {
        Product p2 = new Product(2L, "Cheese", 1.0, Category.DAIRY, null, 5.0);
        Product p3 = new Product(3L, "Butter", 2.0, Category.DAIRY, null, 15.0);

        assertEquals(5, Product.getExtent().size());

        Method clear = Product.class.getDeclaredMethod("clearExtent");
        clear.setAccessible(true);
        clear.invoke(null);

        assertEquals(0, Product.getExtent().size());
    }

    @Test
    void testAddDishToProductBidirectionalConnection() {
        assertTrue(product1.getDishes().isEmpty(), "Product should have no dishes");
        assertTrue(dish1.getProducts().isEmpty(), "Dish should have no products");
        product1.addDish(dish1);
        assertTrue(product1.getDishes().contains(dish1), "Product should contain the dish");
        assertTrue(dish1.getProducts().contains(product1), "Dish should contain the product");
        assertEquals(1, product1.getDishes().size(), "Product should have one dish");
        assertEquals(1, dish1.getProducts().size(), "Dish should have one product");
    }

    @Test
    void testAddProductToDishBidirectionalConnection() {
        assertTrue(product1.getDishes().isEmpty(), "Product should have no dishes");
        assertTrue(dish1.getProducts().isEmpty(), "Dish should have no products");
        dish1.addProduct(product1);
        assertTrue(product1.getDishes().contains(dish1), "Product should contain the dish");
        assertTrue(dish1.getProducts().contains(product1), "Dish should contain the product");
        assertEquals(1, product1.getDishes().size(), "Product should have one dish");
        assertEquals(1, dish1.getProducts().size(), "Dish should have one product");
    }

    @Test
    void testRemoveDishFromProductBidirectionalConnection() {
        product1.addDish(dish1);
        assertTrue(product1.getDishes().contains(dish1), "Connection should be established");
        product1.removeDish(dish1);
        assertFalse(product1.getDishes().contains(dish1), "Product should no longer contain the dish");
        assertFalse(dish1.getProducts().contains(product1), "Dish should no longer contain the product");
        assertTrue(product1.getDishes().isEmpty(), "Product's dishes set should be empty");
        assertTrue(dish1.getProducts().isEmpty(), "Dish's products set should be empty");
    }

    @Test
    void testMultipleProductsAndDishesAssociation() {
        dish1.addProduct(product1);
        dish1.addProduct(product2);
        product1.addDish(dish2);
        assertEquals(2, dish1.getProducts().size(), "Dish1 should have two products");
        assertTrue(dish1.getProducts().contains(product1), "Dish1 should contain product1");
        assertTrue(dish1.getProducts().contains(product2), "Dish1 should contain product2");
        assertEquals(2, product1.getDishes().size(), "Product1 should have two dishes");
        assertTrue(product1.getDishes().contains(dish1), "Product1 should contain dish1");
        assertTrue(product1.getDishes().contains(dish2), "Product1 should contain dish2");
        assertEquals(1, product2.getDishes().size(), "Product2 should have one dish");
        assertTrue(product2.getDishes().contains(dish1), "Product2 should contain dish1");
    }

}