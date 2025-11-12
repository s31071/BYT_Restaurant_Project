package classes;

import java.time.LocalDateTime;

enum ReservationStatus {
    AVAILABLE, TAKEN, RESERVED
}

public class Reservation {
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
    }

    private void manageCustomerReservation(){
        switch(this.status) {
            case AVAILABLE -> System.out.println("available");
            case RESERVED -> System.out.println("reserved");
            case TAKEN -> System.out.println("taken");
        }
    }
    private void createReservation(){
        if(this.status == ReservationStatus.AVAILABLE) {
            this.status = ReservationStatus.RESERVED;
        }
    }

    // co tu sie wstawia?
    private void modifyReservation(){}
    private void cancelReservation(){
        if(this.status == ReservationStatus.RESERVED) this.status = ReservationStatus.AVAILABLE;
    }
}
