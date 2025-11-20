package test;

import classes.Dish;
import classes.Menu;
import classes.MenuType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    private Menu foodMenu;
    private Menu beverageMenu;
    private Menu seasonalMenu;
    private Dish dish1;
    private Dish dish2;

    @BeforeEach
    void setUp() throws Exception {

        Method clearMenu = Menu.class.getDeclaredMethod("clearExtent");
        clearMenu.setAccessible(true);
        clearMenu.invoke(null);

        Method clearDish = Dish.class.getDeclaredMethod("clearExtent");
        clearDish.setAccessible(true);
        clearDish.invoke(null);

        foodMenu = new Menu("Main Menu", MenuType.FOOD);
        beverageMenu = new Menu("Drink Menu", MenuType.BEVERAGE);
        seasonalMenu = new Menu("Seasonal Menu", MenuType.SEASONAL);

        dish1 = new Dish("Dish1", 12.00);
        dish2 = new Dish("Dish2", 2.00);
    }

    @Test
    void testConstructor() {
        assertEquals("Main Menu", foodMenu.getName());
        assertEquals(MenuType.FOOD, foodMenu.getType());
    }

    @Test
    void testConstructorAllTypes() {
        assertEquals(MenuType.FOOD, foodMenu.getType());
        assertEquals(MenuType.BEVERAGE, beverageMenu.getType());
        assertEquals(MenuType.SEASONAL, seasonalMenu.getType());
    }

    @Test
    void testGetName() {
        assertEquals("Main Menu", foodMenu.getName());
        assertEquals("Drink Menu", beverageMenu.getName());
        assertEquals("Seasonal Menu", seasonalMenu.getName());
    }

    @Test
    void testGetType() {
        assertEquals(MenuType.FOOD, foodMenu.getType());
        assertEquals(MenuType.BEVERAGE, beverageMenu.getType());
        assertEquals(MenuType.SEASONAL, seasonalMenu.getType());
    }

    @Test
    void testConstructorWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Menu("", MenuType.BEVERAGE));
    }

    @Test
    void testMenuInstanceSeparation() {
        Menu menu1 = new Menu("Test Menu", MenuType.FOOD);
        Menu menu2 = new Menu("Test Menu", MenuType.FOOD);

        assertNotSame(menu1, menu2);
        assertEquals(menu1.getName(), menu2.getName());
        assertEquals(menu1.getType(), menu2.getType());
    }

    @Test
    void testMenuTypeEnumValues() {
        MenuType[] types = MenuType.values();
        assertEquals(3, types.length);

        assertTrue(containsType(types, MenuType.FOOD));
        assertTrue(containsType(types, MenuType.BEVERAGE));
        assertTrue(containsType(types, MenuType.SEASONAL));
    }

    @Test
    void testMenuTypeValueOf() {
        assertEquals(MenuType.FOOD, MenuType.valueOf("FOOD"));
        assertEquals(MenuType.BEVERAGE, MenuType.valueOf("BEVERAGE"));
        assertEquals(MenuType.SEASONAL, MenuType.valueOf("SEASONAL"));
    }

    @Test
    void testMenuTypeValueOfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> MenuType.valueOf("INVALID"));
    }

    private boolean containsType(MenuType[] types, MenuType target) {
        for (MenuType type : types) {
            if (type == target) {
                return true;
            }
        }
        return false;
    }

    @Test
    void testAddExtent() {
        int initialSize = Menu.getExtent().size();
        Menu newMenu = new Menu("New Menu", MenuType.FOOD);
        assertEquals(initialSize + 1, Menu.getExtent().size());
        assertTrue(Menu.getExtent().contains(newMenu));
    }

    @Test
    void testRemoveFromExtent() {
        Menu menuToRemove = new Menu("Temporary Menu", MenuType.FOOD);
        assertTrue(Menu.getExtent().contains(menuToRemove));

        Menu.removeFromExtent(menuToRemove);
        assertFalse(Menu.getExtent().contains(menuToRemove));
    }

    @Test
    void testGetExtentIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            Menu.getExtent().add(new Menu("Test", MenuType.FOOD));
        });
    }
}