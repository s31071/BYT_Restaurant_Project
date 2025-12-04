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
    private HashMap<LocalDateTime,Order> orders = new HashMap<>();
    private Waiter waiter; //many to one with Waiter
    private Customer customer; //many to one with Customer
    public HashSet<Reservation> reservations = new HashSet<>();
    public Table(){}
    public Table(int number, int numberOfSeats, TableStatus status, LocalDateTime date) {
        setNumber(number);
        setNumberOfSeats(numberOfSeats);
        setStatus(status);
        setDate(date);
        this.waiter = null;
        this.customer = null;
//        this.orders = new HashMap<>(); // iffy podejscie ale bedzie mozna poprawic
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

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public Waiter getWaiter() {
        return waiter;
    }

    public Customer getCustomer() {
        return customer;
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

    public void addManagedTableReservation(Reservation reservation) throws Exception {
        if(reservation == null){
            throw new Exception("Reservation cannot be null");
        }
        reservations.add(reservation);
        reservation.setTableAssigned(this);
    }

    public void removeManagedTableReservation(Reservation reservation) throws Exception {
        if(reservation == null){
            throw new Exception("Reservation cannot be null");
        }
        if(!reservations.contains(reservation)){
            throw new Exception("This reservation is not managed by this waiter");
        }
        reservations.remove(reservation);
        reservation.setTableAssigned(null);
    }

    public void assignCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        if (this.customer != null) {
            this.customer.removeTable(this);
        }

        this.customer = customer;
        customer.addTable(this); //reverse connection
    }

    public void removeCustomer() {
        if (this.customer != null) {
            Customer temp = this.customer;
            this.customer = null;
            temp.removeTable(this); //reverse connection
        }
    }

    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        LocalDateTime timestamp = order.getTimestamp();
        if (timestamp == null) {
            throw new IllegalArgumentException("Order timestamp cannot be null");
        }

        if (orders.containsKey(timestamp)) {
            throw new IllegalArgumentException("An order already exists for this timestamp at this table");
        }

        orders.put(timestamp, order);
        order.setTable(this); //reverse connection
    }

    public void removeOrder(LocalDateTime timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }

        Order order = orders.remove(timestamp);
        if (order != null) {
            order.setTable(null); //reverse connection
        }
    }

    public Order getOrderByTimestamp(LocalDateTime timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        return orders.get(timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return getNumber() == table.getNumber() && getNumberOfSeats() == table.getNumberOfSeats() && getStatus() == table.getStatus() && Objects.equals(getDate(), table.getDate()) && Objects.equals(getOrders(), table.getOrders()) && Objects.equals(getWaiter(), table.getWaiter()) && Objects.equals(getCustomer(), table.getCustomer()) && Objects.equals(reservations, table.reservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber(), getNumberOfSeats(), getStatus(), getDate(), getOrders(), getWaiter(), getCustomer(), reservations);
    }
}