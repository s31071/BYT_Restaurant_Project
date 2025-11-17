package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductOrderTest {

    private ProductOrder order;
    private Product p1;
    private Product p2;

    @BeforeEach
    void setup() throws Exception {
        Field productExtent = Product.class.getDeclaredField("extent");
        productExtent.setAccessible(true);
        ((List<?>) productExtent.get(null)).clear();

        Field orderExtent = ProductOrder.class.getDeclaredField("extent");
        orderExtent.setAccessible(true);
        ((List<?>) orderExtent.get(null)).clear();

        p1 = new Product(1, "Milk", 1.0, Category.DAIRY);
        p2 = new Product(2, "Bread", 0.5, Category.BREAD);
        order = new ProductOrder(List.of(p1, p2));
    }

    @Test
    void testConstructorValid() {
        double expectedWeight = p1.getWeight() + p2.getWeight();
        assertEquals(List.of(p1, p2), order.getProducts());
        assertEquals(expectedWeight, order.getWeight());
    }

    @Test
    void testSetProductsValid() {
        Product p3 = new Product(3, "Butter", 0.2, Category.DAIRY);
        Product p4 = new Product(4, "Eggs", 0.6, Category.DAIRY);

        order.setProducts(List.of(p3, p4));

        assertEquals(List.of(p3, p4), order.getProducts());
        assertEquals(p3.getWeight() + p4.getWeight(), order.getWeight());
    }

    @Test
    void testSetProductsNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> order.setProducts(null));
    }

    @Test
    void testSetProductsEmptyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> order.setProducts(List.of()));
    }

    @Test
    void testSetProductsWithNullElementThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> order.setProducts(List.of(p1, null)));
    }
}