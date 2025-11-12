package classes;

import java.time.LocalDateTime;

enum OrderStatus {
    TAKEN, IN_PREPARATION, READY, SERVED, DELIVERED
}

public class Order {
    private int id;
    private int numberOfPeople;
    private OrderStatus status;
    private int quantity;
    private LocalDateTime timestamp;
    private Table table;

    public Order(int id, int numberOfPeople, OrderStatus status, int quantity, LocalDateTime timestamp, Table table) {
        this.id = id;
        this.numberOfPeople = numberOfPeople;
        this.status = status;
        this.quantity = quantity;
        this.timestamp = LocalDateTime.now();
        this.table = table;
    }

    public Order(int id, int numberOfPeople, OrderStatus status, int quantity) {
        this.id = id;
        this.numberOfPeople = numberOfPeople;
        this.status = status;
        this.quantity = quantity;
    }


    private void updateOrderStatus(OrderStatus status){

    }

    private void assignToDeliveryDriver(){}
    private void updateDeliveryStatus(){}
    //que??
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

    //cokolwiek żeby sie nie swiecilo na czerwono
    public double getTotalPrice(){
        double total = 0;
        return total;
    }

}
