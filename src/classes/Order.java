package classes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class Order implements Serializable {
    private static List<Order> extent = new ArrayList<>();

    private int id;
    private int numberOfPeople;
    private OrderStatus status;
   //nie wiem co to opisuje
    //może powinno być many to many Dish-Order
    private int quantity;
    private LocalDateTime timestamp;
    private Table table;
    private DeliveryDriver deliveryDriver;

    public Order(int id, int numberOfPeople, OrderStatus status, int quantity, LocalDateTime timestamp, Table table) {
        this.id = id;
        this.numberOfPeople = numberOfPeople;
        this.status = status;
        this.quantity = quantity;
        this.timestamp = LocalDateTime.now();
        this.table = table;
        addExtent(this);
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

    public static double getTotalPrice(){
        double total = 0;
        return total;
    }

    public static void addExtent(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        extent.add(order);
    }

    public static List<Order> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Order order) {
        extent.remove(order);
    }

    public static void writeExtent(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Order>) objectInputStream.readObject();
    }
}