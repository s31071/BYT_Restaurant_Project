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
        setId(id);
        setNumberOfPeople(numberOfPeople);
        setStatus(status);
        setQuantity(quantity);
        setTimestamp(timestamp);
        setTable(table);
        setDeliveryDriver(null);
        addExtent(this);
    }

    public void setId(int id) {
        if(id < 0 || id >= extent.size()) {
            try {
                throw new IllegalIdException("Invalid ID");
            } catch (IllegalIdException e) {
                throw new RuntimeException(e);
            }
        }
        this.id = id;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        if(numberOfPeople < 0 || numberOfPeople >= extent.size()) {
            throw new RuntimeException("Invalid number of people");
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
        if(quantity < 0 || quantity >= extent.size()) {
            throw new RuntimeException("Invalid quantity");
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
        if(deliveryDriver == null) {
            throw new NullPointerException("Invalid delivery driver");
        }
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

    public static void writeExtent(XMLEncoder objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(XMLDecoder objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Order>) objectInputStream.readObject();
    }
}