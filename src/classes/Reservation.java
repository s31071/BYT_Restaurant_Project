package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.IOException;
import java.util.Objects;

public class Reservation implements Serializable {
    private static List<Reservation> extent = new ArrayList<>();

    private int id;
    // druga opcja: private Customer customer i później walnąć getSurname() metode
    private String nameOfThePerson;
    private LocalDateTime timestamp;
    private ReservationStatus status;
    private Waiter waiterAssigned;
    private Table table;

    public Waiter getWaiterAssigned() {
        return waiterAssigned;
    }

    public Reservation(){}
    public Reservation(int id, String nameOfThePerson, LocalDateTime timestamp, ReservationStatus status) {
        setId(id);
        setNameOfThePerson(nameOfThePerson);
        setTimestamp(timestamp);
        setStatus(status);
        addExtent(this);
    }

    public void setWaiterAssigned(Waiter waiterAssigned){
        this.waiterAssigned = waiterAssigned;
    }

    public void addWaiterManaging(Waiter waiter) throws Exception {
        if(this.waiterAssigned == null) {
            setWaiterAssigned(waiter);
            waiter.getReservations().add(this);
        }else{
            throw new Exception("This reservation has already a waiter assigned");
        }
    }

    public void removeWaiterManaging() throws Exception {
        if(this.waiterAssigned == null){
            throw new Exception("There is no waiter assigned for this reservation yet");
        }
        this.waiterAssigned.getReservations().remove(this);
        this.waiterAssigned = null;
    }

    public void setTableAssigned(Table table){
        this.table = table;
    }

    public void addTableAssigned(Table table) throws Exception {
        if(this.table == null) {
           setTableAssigned(table);
           table.reservations.add(this);
        } else {
            throw new Exception("This reservation already has a table assigned");
        }

    }

    public void removeTableAssigned() throws Exception {
        if(this.table == null) {
            throw new Exception("There is no table assigned to this reservation");
        }
        this.table.reservations.remove(this);
        this.table = null;
    }

    public void setId(int id) {
        if(id < 0 && id >= extent.size()) {
            throw new IllegalArgumentException("The given id is out of bounds.");
        }
        this.id = id;
    }

    public void setNameOfThePerson(String nameOfThePerson) {
        if(nameOfThePerson.isEmpty()) {
            throw new IllegalArgumentException("The name of the person cannot be empty");
        }
        this.nameOfThePerson = nameOfThePerson;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        if(timestamp == null) {
            throw new IllegalArgumentException("The timestamp cannot be null");
        }
        this.timestamp = timestamp;
    }

    public void setStatus(ReservationStatus status) {
        if(status == null) {
            throw new IllegalArgumentException("The status cannot be null");
        }
        this.status = status;
    }

    private void manageCustomerReservation(String action, String name, LocalDateTime timestamp){
        switch(this.status) {
            case AVAILABLE -> {
                System.out.println("available");
                if(action.equals("add")) createReservation();
            }
            case RESERVED -> {
                System.out.println("reserved");
                if(action.equals("modify")) modifyReservation(name, timestamp);
                else if(action.equals("cancel")) cancelReservation();
            }
            case TAKEN -> System.out.println("taken");
        }
    }

    private void createReservation(){
        if(this.status == ReservationStatus.AVAILABLE) {
            this.status = ReservationStatus.RESERVED;
        }
        addExtent(this);
    }

    private void modifyReservation(String name, LocalDateTime timestamp){
        if(this.status == ReservationStatus.RESERVED) {
            if(name != null) {
                this.nameOfThePerson = name;
            }
            if(timestamp != null) {
                this.timestamp = timestamp;
            }
        } else {
            throw new RuntimeException("Error modifying this reservation");
        }
    }

    private void cancelReservation(){
        if(this.status == ReservationStatus.RESERVED) this.status = ReservationStatus.AVAILABLE;
        removeFromExtent(this);
    }

    public int getId() {
        return id;
    }

    public String getNameOfThePerson() {
        return nameOfThePerson;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public Table getTable() {
        return table;
    }

    public static void addExtent(Reservation reservation) {
        if(reservation == null){
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        if(extent.contains(reservation)){
            throw new IllegalArgumentException("Such reservation is already in data base");
        }
        extent.add(reservation);
    }


    public static List<Reservation> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Reservation reservation) {
        extent.remove(reservation);
    }

    public static void writeExtent(XMLEncoder objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(XMLDecoder objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Reservation>) objectInputStream.readObject();
    }

    public static void clearExtent(){
        extent.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return getId() == that.getId() && Objects.equals(getNameOfThePerson(), that.getNameOfThePerson()) && Objects.equals(getTimestamp(), that.getTimestamp()) && getStatus() == that.getStatus() && Objects.equals(getWaiterAssigned(), that.getWaiterAssigned()) && Objects.equals(getTable(), that.getTable());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNameOfThePerson(), getTimestamp(), getStatus(), getWaiterAssigned(), getTable());
    }
}