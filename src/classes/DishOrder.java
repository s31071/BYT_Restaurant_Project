package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DishOrder implements Serializable {
    private Dish dish;
    private int quantity;
    private static List<DishOrder> extent = new ArrayList<>();

    public DishOrder(){}
    public DishOrder(Dish dish, int quantity) {
        this.dish = dish;
        this.quantity = quantity;
    }

    public Dish getDish() { return dish; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
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

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static List<DishOrder> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(DishOrder DishOrder) {
        extent.remove(DishOrder);
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
}