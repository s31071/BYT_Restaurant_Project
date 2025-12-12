package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.*;
import java.io.Serializable;
import java.io.IOException;
import java.util.Objects;

public class Menu implements Serializable {
    private static List<Menu> extent = new ArrayList<>();

    private String name;
    private MenuType type;
    private HashSet<Dish> dishes = new HashSet<>();

    public Menu(){}
    public Menu(String name, MenuType type) {
        setName(name);
        setType(type);
        addExtent(this);
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
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

    public HashSet<Dish> getDishes() {
        return dishes;
    }

    public void addManagedDish(Dish dish) {
        if (dish == null) {
            throw new IllegalArgumentException("Dish cannot be null");
        }
        if(!dishes.contains(dish)){
            dishes.add(dish);
            if (dish.getMenu() != this) {
                dish.addMenuManaging(this);
            }
        }
    }

    public void removeManagedDish(Dish dish) {
        if (dish == null) {
            throw new IllegalArgumentException("Dish cannot be null");
        }

        if (dishes.remove(dish)) {
            if (dish.getMenu() == this) {
                dish.removeMenuManaging(this);
            }
        }
    }


    public boolean containsDish(Dish dish) {
        return dishes.contains(dish);
    }

    public int getDishCount() {
        return dishes.size();
    }


    public static void addExtent(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("Menu cannot be null");
        }
        if (extent.contains(menu)) {
            throw new IllegalArgumentException("Such menu is already in database");
        }
        extent.add(menu);
    }

    public static List<Menu> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Menu menu) {
        if (menu == null) {
            return;
        }
        List<Dish> dishesToDelete = new ArrayList<>(menu.dishes);
        for (Dish dish : dishesToDelete) {
            menu.removeManagedDish(dish);
        }
        extent.remove(menu);
    }

    public static void writeExtent(XMLEncoder objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(XMLDecoder objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Menu>) objectInputStream.readObject();
    }

    public static void clearExtent() {
        extent.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(name, menu.name) && type == menu.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}