package classes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum TableStatus {
    AVAILABLE, TAKEN, RESERVED
}

public class Table {
    private int number;
    private int numberOfSeats;
    private TableStatus status;
    private LocalDateTime date;
    private HashMap<LocalDateTime,Order> orders;

    public Table(int number, int numberOfSeats, TableStatus status, LocalDateTime date) {
        this.number = number;
        this.numberOfSeats = numberOfSeats;
        this.status = status;
        this.date = date;
        this.orders = new HashMap<>(); // iffy podejscie ale bedzie mozna poprawic
    }

    //co to znaczy create albo delete table bo nie mam pojecia :')
    private void createTable(){}
    private void changeTableStatus(TableStatus status){
        this.status = status;
    }
    private void deleteTable(){}
    private void deleteOrder(LocalDateTime timestamp){
        Order removed = this.orders.remove(timestamp);
    }
    private void takeOrder(Order order){
        this.orders.put(order.getTimestamp(), order);
    }
    private OrderStatus accessOrderStatus(Order order){
        return order.getStatus();
    }
    private void displayAllOrders(){
        for(Map.Entry<LocalDateTime, Order> entry : orders.entrySet()) {
            Order order = entry.getValue();
            System.out.println("Timestamp: " + entry.getKey() +
                    " | Order #" + order.getId() +
                    " | Status: " + order.getStatus() +
                    " | People: " + order.getNumberOfPeople());
        }
    }


}
