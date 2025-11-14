package classes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reservation {
    private int id;
    // druga opcja: private Customer customer i później walnąć getSurname() metode
    private String nameOfThePerson;
    private LocalDateTime timestamp;
    private ReservationStatus status;
    private static List<Reservation> reservations = new ArrayList<>();
    public Reservation(int id, String nameOfThePerson, LocalDateTime timestamp, ReservationStatus status) {
        this.id = id;
        this.nameOfThePerson = nameOfThePerson;
        this.timestamp = timestamp;
        this.status = status;
        reservations.add(this);
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
        reservations.add(this);
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
        reservations.remove(this);
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

    public static List<Reservation> getReservations() {
        return reservations;
    }
}
