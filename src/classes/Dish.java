package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.*;
import java.io.Serializable;
import java.io.IOException;

public class Dish implements Serializable {
    private static List<Dish> extent = new ArrayList<>();

    private String name;
    private double price;
    private List<Integer> reviews = new ArrayList<>();
    private Menu menu; //composition with Menu
    private Set<DishOrder> dishOrders = new HashSet<>(); //aggregation with Order

    public Dish(){}
    public Dish(String name, double price, List<Integer> reviews) {
        setName(name);
        setPrice(price);
        setReviews(reviews);
        this.menu = null;
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

    public void setReviews(List<Integer> reviews) {
        if (reviews.isEmpty()) {
            throw new IllegalArgumentException("Reviews cannot be empty");
        }
        this.reviews = reviews;
    }

    public void setMenu(Menu menu) throws Exception {
        if(menu == null){
            throw new Exception("Menu cannot be null");
        }
        this.menu = menu;
    }

    public void addMenuManaging(Menu menu) throws Exception {
        if(this.menu == null) {
            setMenu(menu);

            if(!menu.getDishes().contains(this)){
                menu.addManagedDish(this);
            }
        }else if (this.menu != menu) {
            throw new Exception("This dish already has a menu assigned");
        }
    }

    public void removeMenuManaging(Menu menu) throws Exception {
        if (menu == null) {
            throw new Exception("Menu cannot be null");
        }

        if (this.menu != menu) {
            return;
        }

        this.menu = null;

        if (menu.getDishes().contains(this)) {
            menu.removeManagedDish(this);
        }
    }

    public void setDishOrders(Set<DishOrder> dishOrders) {
        this.dishOrders = dishOrders;
    }

    public static boolean checkAvailability(Dish dish) {
        if (extent.contains(dish)) return true;
        return false;
    }

    public void addManagedDishOrder(DishOrder dishOrder) throws Exception {
        if (dishOrder == null) {
            throw new Exception("DishOrder cannot be null");
        }
        if(!dishOrders.contains(dishOrder)){
            dishOrders.add(dishOrder);
            if(dishOrder.getDish() != this) {
                dishOrder.addDishManaging(this);
            }
        }
    }

    public void removeManagedDishOrder(DishOrder dishOrder) throws Exception {
        if (dishOrder == null) {
            throw new Exception("DishOrder cannot be null");
        }

        if (dishOrders.remove(dishOrder)) {
            if (dishOrder.getDish() == this) {
                dishOrder.removeDishManaging(this);
            }
        }
    }

    public void addDishOrderDish(DishOrder dishOrder) {
        if (dishOrder == null) {
            throw new IllegalArgumentException("DishOrder cannot be null");
        }
        dishOrders.add(dishOrder);
    }


    public void removeDishOrderDish(DishOrder dishOrder) {
        if (dishOrder == null) {
            throw new IllegalArgumentException("DishOrder cannot be null");
        }
        dishOrders.remove(dishOrder);

    }

    public static void addNewDish(Dish dish) {
        extent.add(dish);
    }

    public static void deleteDish(Dish dish) {
        if(extent.contains(dish)) extent.remove(dish);
        else throw new IllegalArgumentException("Dish could not be found");
    }

    public static List<Dish> getDishList() {
        return Collections.unmodifiableList(extent);
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public List<Integer> getReviews() {
        return reviews;
    }
    public Menu getMenu() {
        return menu;
    }

    public Set<DishOrder> getDishOrders() {
        //return dishOrders;
        return Collections.unmodifiableSet(new HashSet<>(dishOrders));
    }

    public static void addExtent(Dish dish) {
        if(dish == null){
            throw new IllegalArgumentException("Dish cannot be null");
        }
        if(extent.contains(dish)){
            throw new IllegalArgumentException("Such dish is already in data base");
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

    public static void clearExtent() {
        extent.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Double.compare(price, dish.price) == 0 && Objects.equals(name, dish.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}