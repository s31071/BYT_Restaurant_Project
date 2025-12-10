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
        try {
            setDish(dish);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            setOrder(order);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setQuantity(quantity);
        addExtent(this);
    }

    public Dish getDish() { return dish; }
    public Order getOrder() {
        return order;
    }
    public int getQuantity() { return quantity; }


    public void setDish(Dish dish) throws Exception {
        if(dish == null) {
            throw new IllegalArgumentException("Dish cannot be null");
        }
        this.dish = dish;
    }

    public void addDishManaging(Dish dish) throws Exception {
        if(this.dish == null) {
            setDish(dish);

            if(!dish.getDishOrders().contains(this)){
                dish.addManagedDishOrder(this);
            }
        } else if (this.dish != dish) {
            throw new Exception("This DishOrder already has a dish assigned");
        }
    }

    public void removeDishManaging(Dish dish) throws Exception {
        if (dish == null) {
            throw new Exception("Dish cannot be null");
        }

        if (this.dish != dish) {
            return;
        }

        this.dish = null;

        if (dish.getDishOrders().contains(this)) {
            dish.removeManagedDishOrder(this);
        }
    }



    public void setOrder(Order order) throws Exception {
        if(order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        this.order = order;
    }

    public void addOrderManaging(Order order) throws Exception {
        if(this.order == null) {
            setOrder(order);

            if(!order.getDishOrders().contains(this)){
                order.addManagedDishOrder(this);
            }
        }else if (this.order != order) {
            throw new Exception("This DishOrder already has an order assigned");
        }
    }

    public void removeOrderManaging(Order order) throws Exception {
        if (order == null) {
            throw new Exception("Order cannot be null");
        }

        if (this.order != order) {
            return;
        }

        this.order = null;

        if (order.getDishOrders().contains(this)) {
            order.removeManagedDishOrder(this);
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
        boolean result = Objects.equals(dish, dishOrder.dish) && Objects.equals(order, dishOrder.order);
        return result;
    }



    @Override
    public int hashCode() {
        return Objects.hash(dish, order);
    }



}