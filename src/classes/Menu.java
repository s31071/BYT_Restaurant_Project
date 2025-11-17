package classes;

import javax.management.AttributeNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class Menu implements Serializable {
    private static List<Menu> extent = new ArrayList<>();

    private String name;
    private MenuType type;

    public Menu(String name, MenuType type) {
        this.name = name;
        this.type = type;
        addExtent(this);
    }

    public static void updateMenu(Dish dish, String action) throws AttributeNotFoundException {
        if (!Dish.getDishList().contains(dish)) {
            if (action.equals("ADD")) {
                Dish.addNewDish(dish);
            } else if (action.equals("DELETE")) {
                Dish.deleteDish(dish);
            }
        } else {
            throw new AttributeNotFoundException("Could not find dish to update");
        }
    }

    public String getName() {
        return name;
    }

    public MenuType getType() {
        return type;
    }

    public static void addExtent(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("Menu cannot be null");
        }
        extent.add(menu);
    }

    public static List<Menu> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Menu menu) {
        extent.remove(menu);
    }

    public static void writeExtent(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(ObjectInputStream objectInputStream)
            throws IOException, ClassNotFoundException {
        extent = (List<Menu>) objectInputStream.readObject();
    }
}