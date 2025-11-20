package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;

public class Order implements Serializable {
    private static List<Order> extent = new ArrayList<>();
    private List<Dish> dishes = new ArrayList<>();

    private int id;
    private int numberOfPeople;
    private OrderStatus status;
    private int quantity;
    private LocalDateTime timestamp;
    private Table table;
    private DeliveryDriver deliveryDriver;

    public Order(int id, int numberOfPeople, OrderStatus status, int quantity, LocalDateTime timestamp, Table table) {
        setId(id);
        setNumberOfPeople(numberOfPeople);
        setStatus(status);
        setQuantity(quantity);
        setTimestamp(timestamp);
        setTable(table);
        setDeliveryDriver(null);
        addExtent(this);
    }

    public Order(int id, int numberOfPeople, OrderStatus status, int quantity, LocalDateTime timestamp) {
        setId(id);
        setNumberOfPeople(numberOfPeople);
        setStatus(status);
        setQuantity(quantity);
        setTimestamp(timestamp);
        setDeliveryDriver(null);
        addExtent(this);
    }

    public void setId(int id) {
        if(id < 0 && id >= extent.size()) {
            throw new IllegalArgumentException("The given id is out of bounds.");
        }
        this.id = id;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        if(numberOfPeople <= 0 && numberOfPeople >= extent.size()) {
            throw new NullPointerException("Invalid number of people");
        }
        this.numberOfPeople = numberOfPeople;
    }

    public void setStatus(OrderStatus status) {
        if(status == null) {
            throw new NullPointerException("Invalid status");
        }
        this.status = status;
    }

    public void setQuantity(int quantity) {
        if(quantity <= 0 && quantity >= extent.size()) {
            throw new NullPointerException("Invalid quantity");
        }
        this.quantity = quantity;
    }

    public void setTable(Table table) {
        if(table == null) {
            throw new NullPointerException("Invalid table");
        }
        this.table = table;
    }

    public void setDeliveryDriver(DeliveryDriver deliveryDriver) {
        this.deliveryDriver = deliveryDriver;
    }

    public void updateOrderStatus(OrderStatus status){
        this.status = status;
    }

    public void assignToDeliveryDriver(DeliveryDriver deliveryDriver){
        this.deliveryDriver = deliveryDriver;
    }

    private void finalizeOrder(){}

    public LocalDateTime getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Table getTable(){
        return table;
    }

    public OrderStatus getStatus(){
        return this.status;
    }

    public int getId() {
        return id;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public double getTotalPrice(){
        double total = 0;
        for (Dish dish : dishes) {
            total += dish.getPrice();
        }
        return total;
    }

    public static void addExtent(Order order) {
        if(order == null){
            throw new IllegalArgumentException("Order cannot be null");
        }
        if(extent.contains(order)){
            throw new IllegalArgumentException("Such order is already in data base");
        }
        extent.add(order);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static List<Order> getOrders() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Order order) {
        extent.remove(order);
    }

    public static void writeExtent(XMLEncoder objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(XMLDecoder objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Order>) objectInputStream.readObject();
    }

    public void addDish(Dish dish) {
        if (dish == null) {
            throw new IllegalArgumentException("Dish cannot be null");
        }
        if (!dishes.contains(dish)) {
            dishes.add(dish);
        }
    }

    public void removeDish(Dish dish) {
        dishes.remove(dish);
    }

    public List<Dish> getDishes() {
        return Collections.unmodifiableList(dishes);
    }

    public boolean containsDish(Dish dish) {
        return dishes.contains(dish);
    }

    public int getDishCount() {
        return dishes.size();
    }

    public static void clearExtent() {
        extent.clear();
    }
}