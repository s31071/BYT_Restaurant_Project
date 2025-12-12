package test;

import classes.Dish;
import classes.Menu;
import classes.MenuType;
import classes.Product;
import classes.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DishTest {

    private Dish dish1;
    private Dish dish2;
    private Dish dish3;
    private Menu foodMenu;
    private Menu beverageMenu;
    private List<Integer> reviews = new ArrayList<>(List.of(4, 5, 4, 5, 5, 3));
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() throws Exception {
        Method clearDish = Dish.class.getDeclaredMethod("clearExtent");
        clearDish.setAccessible(true);
        clearDish.invoke(null);

        Method clearMenu = Menu.class.getDeclaredMethod("clearExtent");
        clearMenu.setAccessible(true);
        clearMenu.invoke(null);

        Method clearProduct = Product.class.getDeclaredMethod("clearExtent");
        clearProduct.setAccessible(true);
        clearProduct.invoke(null);

        foodMenu = new Menu("Main Menu", MenuType.FOOD);
        beverageMenu = new Menu("Drink Menu", MenuType.BEVERAGE);

        dish1 = new Dish("Dish1", 15.50, reviews);
        dish2 = new Dish("Dish2", 10.50, reviews);
        dish3 = new Dish("Dish3", 5.50, reviews);

        product1 = new Product(1L, "Tomato", 0.5, Category.VEGETABLES, LocalDate.now().plusDays(10), 2.50);
        product2 = new Product(2L, "Cheese", 0.3, Category.DAIRY, LocalDate.now().plusDays(30), 5.00);
    }

    @Test
    void testConstructor() {
        assertEquals("Dish1", dish1.getName());
        assertEquals(15.50, dish1.getPrice(), 0.001);
    }

    @Test
    void testGetName() {
        assertEquals("Dish1", dish1.getName());
        assertEquals("Dish2", dish2.getName());
    }

    @Test
    void testGetPrice() {
        assertEquals(15.50, dish1.getPrice(), 0.001);
        assertEquals(10.50, dish2.getPrice(), 0.001);
    }

    @Test
    void testGetReviews() {
        List<Integer> dishReviews = dish1.getReviews();
        assertEquals(6, dishReviews.size());
        assertTrue(dishReviews.contains(4));
        assertTrue(dishReviews.contains(5));
    }

    @Test
    void testDishInitiallyHasNoMenu() {
        assertNull(dish1.getMenu(), "Newly created Dish should have no Menu");
    }

    @Test
    void testAddDishToMenuBidirectionalConnection() {
        assertNull(dish1.getMenu(), "Dish should have no Menu");
        assertTrue(foodMenu.getDishes().isEmpty(), "Menu should have no Dishes");
        foodMenu.addManagedDish(dish1);
        assertEquals(foodMenu, dish1.getMenu(), "Dish should now reference the Menu");
        assertTrue(foodMenu.getDishes().contains(dish1), "Menu should contain the Dish");
        assertEquals(1, foodMenu.getDishes().size(), "Menu should have one Dish");
    }

    @Test
    void testAddMultipleDishesToMenu() {
        foodMenu.addManagedDish(dish1);
        foodMenu.addManagedDish(dish2);
        foodMenu.addManagedDish(dish3);

        assertEquals(3, foodMenu.getDishes().size(), "Menu should have three Dishes");
        assertTrue(foodMenu.getDishes().contains(dish1), "Menu should contain dish1");
        assertTrue(foodMenu.getDishes().contains(dish2), "Menu should contain dish2");
        assertTrue(foodMenu.getDishes().contains(dish3), "Menu should contain dish3");

        assertEquals(foodMenu, dish1.getMenu(), "dish1 should reference foodMenu");
        assertEquals(foodMenu, dish2.getMenu(), "dish2 should reference foodMenu");
        assertEquals(foodMenu, dish3.getMenu(), "dish3 should reference foodMenu");
    }

    @Test
    void testRemoveDishFromMenuBidirectionalConnection() {
        foodMenu.addManagedDish(dish1);
        assertEquals(1, foodMenu.getDishes().size(), "Menu should have one Dish");
        assertEquals(foodMenu, dish1.getMenu(), "Dish should reference Menu");

        foodMenu.removeManagedDish(dish1);

        assertNull(dish1.getMenu(), "Dish should no longer reference the Menu");
        assertFalse(foodMenu.getDishes().contains(dish1), "Menu should not contain the Dish");
        assertTrue(foodMenu.getDishes().isEmpty(), "Menu should have no Dishes");
    }

    @Test
    void testDishCannotBeAddedToMultipleMenus() {
        foodMenu.addManagedDish(dish1);
        assertEquals(foodMenu, dish1.getMenu(), "Dish should reference foodMenu");

        Exception exception = assertThrows(Exception.class, () -> {
            dish1.addMenuManaging(beverageMenu);
        });

        assertEquals("This dish already has a menu assigned", exception.getMessage());
        assertEquals(foodMenu, dish1.getMenu(), "Dish should still reference foodMenu");
    }

    @Test
    void testAddNullDishToMenuThrowsException() {
        Exception exception = assertThrows(Exception.class, () -> {
            foodMenu.addManagedDish(null);
        });

        assertEquals("Dish cannot be null", exception.getMessage());
    }

    @Test
    void testRemoveNullDishFromMenuThrowsException() {
        Exception exception = assertThrows(Exception.class, () -> {
            foodMenu.removeManagedDish(null);
        });

        assertEquals("Dish cannot be null", exception.getMessage());
    }

    @Test
    void testPreventDuplicateDishesInMenu() {
        foodMenu.addManagedDish(dish1);
        assertEquals(1, foodMenu.getDishes().size(), "Menu should have one Dish");

        foodMenu.addManagedDish(dish1);

        assertEquals(1, foodMenu.getDishes().size(), "Menu should have one Dish");
        assertTrue(foodMenu.getDishes().contains(dish1), "Menu should contain the Dish");
    }

    @Test
    void testMenuContainsDish() {
        foodMenu.addManagedDish(dish1);
        foodMenu.addManagedDish(dish2);

        assertTrue(foodMenu.containsDish(dish1), "Menu should contain dish1");
        assertTrue(foodMenu.containsDish(dish2), "Menu should contain dish2");
        assertFalse(foodMenu.containsDish(dish3), "Menu should not contain dish3");
    }

    @Test
    void testGetDishCount() {
        assertEquals(0, foodMenu.getDishCount(), "Menu should have 0 Dishes initially");
        foodMenu.addManagedDish(dish1);
        assertEquals(1, foodMenu.getDishCount(), "Menu should have 1 Dish");
        foodMenu.addManagedDish(dish2);
        assertEquals(2, foodMenu.getDishCount(), "Menu should have 2 Dishes");
        foodMenu.removeManagedDish(dish1);
        assertEquals(1, foodMenu.getDishCount(), "Menu should have 1 Dish after removal");
    }

    @Test
    void testReverseConnectionFromDishSide() {
        dish1.addMenuManaging(foodMenu);
        assertEquals(foodMenu, dish1.getMenu(), "Dish should reference Menu");
        assertTrue(foodMenu.getDishes().contains(dish1), "Menu should contain Dish");
    }

    @Test
    void testRemoveMenuManagingFromDishSide() {
        foodMenu.addManagedDish(dish1);
        assertEquals(foodMenu, dish1.getMenu(), "Dish should reference Menu");
        dish1.removeMenuManaging(foodMenu);
        assertNull(dish1.getMenu(), "Dish should no longer reference Menu");
        assertFalse(foodMenu.getDishes().contains(dish1), "Menu should not contain Dish");
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
    void testRemoveProductFromDishBidirectionalConnection() {
        dish1.addProduct(product1);
        assertTrue(product1.getDishes().contains(dish1), "Connection should be established");
        dish1.removeProduct(product1);
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

    @Test
    void testMultipleDishesInMultipleMenus() {
        Dish.addExtent(dish1);
        Dish.addExtent(dish2);
        Dish.addExtent(dish3);

        foodMenu.addManagedDish(dish1);
        foodMenu.addManagedDish(dish2);
        beverageMenu.addManagedDish(dish3);

        assertEquals(2, foodMenu.getDishCount(), "foodMenu should have 2 Dishes");
        assertEquals(1, beverageMenu.getDishCount(), "beverageMenu should have 1 Dish");

        assertEquals(foodMenu, dish1.getMenu(), "dish1 should reference foodMenu");
        assertEquals(foodMenu, dish2.getMenu(), "dish2 should reference foodMenu");
        assertEquals(beverageMenu, dish3.getMenu(), "dish3 should reference beverageMenu");
    }
}