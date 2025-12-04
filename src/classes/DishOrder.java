package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DishOrder implements Serializable {
    private Dish dish;
    private Order order;
    private int quantity;
    private static List<DishOrder> extent = new ArrayList<>();

    public DishOrder(){}
    public DishOrder(Dish dish, Order order, int quantity) {
        setDish(dish);
        setOrder(order);
        setQuantity(quantity);
        addExtent(this);
    }

    public Dish getDish() { return dish; }
    public Order getOrder() {
        return order;
    }
    public int getQuantity() { return quantity; }


    public void setDish(Dish dish) {
        if(dish == null) {
            throw new IllegalArgumentException("Dish cannot be null");
        }
        if(this.dish != null && this.dish != dish) {
            throw new IllegalStateException("Cannot change dish after DishOrder is created");
        }
        this.dish = dish;
        if(!dish.getDishOrders().contains(this)) {
            dish.addDishOrderDish(this);
        }
    }

    public void setOrder(Order order) {
        if(order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if(this.order != null && this.order != order) {
            throw new IllegalStateException("Cannot change order after DishOrder is created");
        }
        this.order = order;
        if(!order.getDishOrders().contains(this)) {
            order.addDishOrderOrder(this);
        }
    }

    public void setQuantity(int quantity) {
        if(quantity <= 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }
    public static void setExtent(List<DishOrder> extent) {
        DishOrder.extent = extent;
    }

    public static void addExtent(DishOrder DishOrder) {
        if(DishOrder == null){
            throw new IllegalArgumentException("DishOrder cannot be null");
        }
        if(extent.contains(DishOrder)){
            throw new IllegalArgumentException("Such DishOrder is already in data base");
        }
        extent.add(DishOrder);
    }

    public static List<DishOrder> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public void removeFromDishAndOrder(){
        if (this.dish != null) {
            this.dish.removeDishOrderDish(this);
        }
        if (this.order != null) {
            this.order.removeDishOrderOrder(this);
        }
    }

    public static void removeFromExtent(DishOrder dishOrder) {
        if(dishOrder != null) {
            dishOrder.removeFromDishAndOrder();
        }
        extent.remove(dishOrder);
    }

    public static void writeExtent(XMLEncoder out) throws java.io.IOException {
        out.writeObject(extent);
    }

    public static void readExtent(XMLDecoder in) throws java.io.IOException, ClassNotFoundException {
        extent = (List<DishOrder>) in.readObject();
    }

    public static void clearExtent(){
        extent.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DishOrder dishOrder = (DishOrder) o;
        return quantity == dishOrder.quantity && Objects.equals(dish, dishOrder.dish) && Objects.equals(order, dishOrder.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dish, order, quantity);
    }
}