package classes;

import javax.management.AttributeNotFoundException;

enum MenuType {
    FOOD, BEVERAGE, SEASONAL

}
public class Menu {
    private String name;
    private MenuType type;
    public Menu (String name, MenuType type) {
        this.name = name;
        this.type = type;
    }

    private static void updateMenu(Dish dish, String action) throws AttributeNotFoundException {
        if(Dish.getDishList().contains(dish)) {
            if(action == "ADD") {
                Dish.addNewDish(dish);
            } else if(action == "DELETE") {
                Dish.deleteDish(dish);
            }
        } else {
            throw new AttributeNotFoundException("Could not find dish to update");
        }

    }
}
