package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.io.Serializable;
import java.io.IOException;

public class Order implements Serializable {
    private static List<Order> extent = new ArrayList<>();
//    private List<DishOrder> dishes = new ArrayList<>();

    private int id;
    private int numberOfPeople;
    private OrderStatus status;
    private LocalDateTime timestamp;
    private Table table; //many to one with Table
    private Receipt receipt; //one to one with Receipt
    private Set<DishOrder> dishOrders = new HashSet<>();
    private DeliveryDriver deliveryDriver;

    public Order(){}
    public Order(int id, int numberOfPeople, OrderStatus status, LocalDateTime timestamp, Table table) {
        setId(id);
        setNumberOfPeople(numberOfPeople);
        setStatus(status);
        setTimestamp(timestamp);
        setTable(table);
        setDeliveryDriver(null);
        addExtent(this);
    }

    public Order(int id, int numberOfPeople, OrderStatus status, LocalDateTime timestamp) {
        setId(id);
        setNumberOfPeople(numberOfPeople);
        setStatus(status);
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

    public void setTable(Table table) {
        if(table == null) {
            throw new NullPointerException("Invalid table");
        }
        this.table = table;
    }

    public void setDeliveryDriver(DeliveryDriver deliveryDriver) {
        if (this.deliveryDriver != null) {
            this.deliveryDriver.orders.remove(this);
        }
        this.deliveryDriver = deliveryDriver;
        if (deliveryDriver != null && !deliveryDriver.orders.contains(this)) {
            deliveryDriver.orders.add(this);
        }
    }


    public Set<DishOrder> getDishOrders() {
        return Collections.unmodifiableSet(new HashSet<>(dishOrders));
    }

    public Receipt getReceipt(){
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        if (receipt == null) {
            throw new IllegalArgumentException("Receipt cannot be null");
        }

        if (this.receipt != null && this.receipt != receipt) {
            throw new IllegalStateException("Order already has a Receipt. Multiplicity 1 requires only one Receipt per Order.");
        }

        this.receipt = receipt;

        if (receipt.getOrder() != this) {
            receipt.setOrder(this);
        }
    }

    public void updateOrderStatus(OrderStatus status){
        this.status = status;
    }

    public void assignToDeliveryDriver(DeliveryDriver deliveryDriver){
        setDeliveryDriver(deliveryDriver);
    }

    private void finalizeOrder(){
        this.status = OrderStatus.SERVED;
    }

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
        for (DishOrder dish : dishOrders) {
            total += dish.getDish().getPrice() * dish.getQuantity();
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

    public void addDishOrderOrder(DishOrder dishOrder) {
        if (dishOrder == null) {
            throw new IllegalArgumentException("DishOrder cannot be null");
        }
        if (dishOrders.contains(dishOrder)) {
            throw new IllegalArgumentException("This DishOrder is already in this order");
        }
        dishOrders.add(dishOrder);
    }

    public void removeDishOrderOrder(DishOrder dishOrder) {
        if (dishOrder == null) {
            throw new IllegalArgumentException("DishOrder cannot be null");
        }
        dishOrders.remove(dishOrder);
    }

    public void addDish(Dish dish, int quantity) {
        if (dish == null) {
            throw new IllegalArgumentException("Dish cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        for (DishOrder dishOrder : dishOrders) {
            if (dishOrder.getDish().equals(dish)) {
                dishOrder.setQuantity(dishOrder.getQuantity() + quantity);
                return;
            }
        }

        dishOrders.add(new DishOrder(dish, this, quantity));
    }

    public void removeDish(Dish dish) {
        if (dish == null) return;

        DishOrder toRemove = null;
        for (DishOrder dishOrder : dishOrders) {
            if (dishOrder.getDish().equals(dish)) {
                toRemove = dishOrder;
                break;
            }
        }

        if (toRemove != null) {
            dishOrders.remove(toRemove);
        }
    }

//    public List<DishOrder> getDishes() {
//        return Collections.unmodifiableList(dishes);
//    }

    public boolean containsDish(Dish dish) {
        if (dish == null) return false;
        for (DishOrder dishOrder : dishOrders) {
            if (dishOrder.getDish().equals(dish)) {
                return true;
            }
        }
        return false;
    }

    public int getDishCount() {
        return dishOrders.size();
    }

    public static void clearExtent() {
        extent.clear();
    }
    public void removeReceipt() {
        if(this.receipt == null) {
            throw new IllegalArgumentException("Cannot remove this receipt");
        }
        Receipt oldReceipt = this.receipt;
        this.receipt = null;

        if(oldReceipt.getOrder() == this) {
            oldReceipt.removeOrder();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && numberOfPeople == order.numberOfPeople && Objects.equals(timestamp, order.timestamp) && Objects.equals(table, order.table) && Objects.equals(receipt, order.receipt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfPeople, timestamp, table, receipt);
    }
}