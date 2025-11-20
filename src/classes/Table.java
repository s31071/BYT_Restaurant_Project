package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.io.Serializable;
import java.io.IOException;

public class Table implements Serializable {
    private static List<Table> extent = new ArrayList<>();

    private int number;
    private int numberOfSeats;
    private TableStatus status;
    private LocalDateTime date;
    //tą hash mape tzreba wywalić i użyć extent, ale to już usisz sama sobie dostosować, bo ja nie chce nic zepsuć
    private HashMap<LocalDateTime,Order> orders;

    public Table(int number, int numberOfSeats, TableStatus status, LocalDateTime date) {
        setNumber(number);
        setNumberOfSeats(numberOfSeats);
        setStatus(status);
        setDate(date);
        this.orders = new HashMap<>(); // iffy podejscie ale bedzie mozna poprawic
        addExtent(this);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setOrders(HashMap<LocalDateTime, Order> orders) {
        this.orders = orders;
    }

    private void changeTableStatus(TableStatus status){
        this.status = status;
    }

    public void deleteOrder(LocalDateTime timestamp){
        Order removed = this.orders.remove(timestamp);
    }

    public void takeOrder(Order order){
        this.orders.put(order.getTimestamp(), order);
    }

    public OrderStatus accessOrderStatus(Order order){
        return order.getStatus();
    }

    public void displayAllOrders(){
        for(Map.Entry<LocalDateTime, Order> entry : orders.entrySet()) {
            Order order = entry.getValue();
            System.out.println("Timestamp: " + entry.getKey() +
                    " | Order #" + order.getId() +
                    " | Status: " + order.getStatus() +
                    " | People: " + order.getNumberOfPeople());
        }
    }

    public int getNumber() {
        return number;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public TableStatus getStatus() {
        return status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public HashMap<LocalDateTime, Order> getOrders() {
        return orders;
    }

    public static void addExtent(Table table) {
        if(table == null){
            throw new IllegalArgumentException("Table cannot be null");
        }
        if(extent.contains(table)){
            throw new IllegalArgumentException("Such table is already in data base");
        }
        extent.add(table);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static List<Table> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Table table) {
        extent.remove(table);
    }

    public static void writeExtent(XMLEncoder objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(XMLDecoder objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Table>) objectInputStream.readObject();
    }

    public static void clearExtent() {
        extent.clear();
    }
}