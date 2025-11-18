package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;

public class Dish implements Serializable {
    private static List<Dish> extent = new ArrayList<>();

    private String name;
    private double price;

    public Dish(String name, double price) {
        setName(name);
        setPrice(price);
        addExtent(this);
    }

    public void setName(String name) {
        if(name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price cannot be negative or empty");
        }
        this.price = price;
    }

    public static boolean checkAvailability(Dish dish) {
        if (extent.contains(dish)) return true;
        return false;
    }

    public static void addNewDish(Dish dish) {
        extent.add(dish);
    }

    public static void deleteDish(Dish dish) {
        if(extent.contains(dish)) extent.remove(dish);
        else throw new IllegalArgumentException("Dish could not be found");
    }

    public static List<Dish> getDishList() {
        return extent;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public static void addExtent(Dish dish) {
        if (dish == null) {
            throw new IllegalArgumentException("Dish cannot be null");
        }
        extent.add(dish);
    }

    public static List<Dish> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Dish dish) {
        extent.remove(dish);
    }

    public static void writeExtent(XMLEncoder objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(XMLDecoder objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Dish>) objectInputStream.readObject();
    }
}