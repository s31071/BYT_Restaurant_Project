package classes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class Reservation implements Serializable {
    private static List<Reservation> extent = new ArrayList<>();

    private int id;
    // druga opcja: private Customer customer i później walnąć getSurname() metode
    private String nameOfThePerson;
    private LocalDateTime timestamp;
    private ReservationStatus status;

    public Reservation(int id, String nameOfThePerson, LocalDateTime timestamp, ReservationStatus status) {
        this.id = id;
        this.nameOfThePerson = nameOfThePerson;
        this.timestamp = timestamp;
        this.status = status;
        addExtent(this);
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

    public static void addExtent(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        extent.add(reservation);
    }

    public static List<Reservation> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Reservation reservation) {
        extent.remove(reservation);
    }

    public static void writeExtent(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Reservation>) objectInputStream.readObject();
    }
}