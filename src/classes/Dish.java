package classes;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    private static List<Dish> dishList = new ArrayList<>();
    private String name;
    private double price;

    public Dish(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public static boolean checkAvailability(Dish dish) {
        if(dishList.contains(dish)) return true;
        return false;
    }

    public static void addNewDish(Dish dish) {
        dishList.add(dish);
    }

    public static void deleteDish(Dish dish) {
        if(dishList.contains(dish)) dishList.remove(dish);
        else throw new IllegalArgumentException("Dish could not be found");
    }

    public static List<Dish> getDishList() {
        return dishList;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
