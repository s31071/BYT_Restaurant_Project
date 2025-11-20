package classes;

import javax.management.AttributeNotFoundException;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;

public class Menu implements Serializable {
    private static List<Menu> extent = new ArrayList<>();

    private String name;
    private MenuType type;

    public Menu(String name, MenuType type) {
        setName(name);
        setType(type);
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

    public void setName(String name) {
        if (name.isEmpty() || name == null) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public void setType(MenuType type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public MenuType getType() {
        return type;
    }

    public static void addExtent(Menu menu) {
        if(menu == null){
            throw new IllegalArgumentException("Menu cannot be null");
        }
        if(extent.contains(menu)){
            throw new IllegalArgumentException("Such menu is already in data base");
        }
        extent.add(menu);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static List<Menu> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Menu menu) {
        extent.remove(menu);
    }

    public static void writeExtent(XMLEncoder objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(XMLDecoder objectInputStream)
            throws IOException, ClassNotFoundException {
        extent = (List<Menu>) objectInputStream.readObject();
    }

    public static void clearExtent() {
        extent.clear();
    }
}