package classes;

import java.util.Objects;

public abstract class Person {

    public String name;
    public String surname;
    public String phoneNumber;
    public Address address;
    public String email;

    public Person(){}
    public Person(String name, String surname, String phoneNumber, String street, String city, String postalCode, String country, String email) {
        setName(name);
        setSurname(surname);
        setPhoneNumber(phoneNumber);
        Address address = new Address(street, city, postalCode, country);
        setAddress(address);
        setEmail(email);
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (!name.matches("^[A-Za-z]+$")) {
            throw new IllegalArgumentException(
                    "Name can only contain letters A–Z or a–z"
            );
        }
        this.name = name;
    }

    public void setSurname(String surname) {
        if (surname == null || surname.isBlank()) {
            throw new IllegalArgumentException("Surname cannot be empty");
        }
        if (!surname.matches("^[A-Za-z-]+$")) {
            throw new IllegalArgumentException(
                    "Surname can only contain letters and '-' sign"
            );
        }
        this.surname = surname;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("\\d{9}")) {
            throw new IllegalArgumentException("Telephone number has to contains 9 digits");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(Address address) {
        if(address == null){
            throw new IllegalArgumentException("Address cannot be null");
        }
        this.address = address;
    }

    public void setEmail(String email) {
        if(email == null){
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (!email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Incorrect email format");
        }
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(surname, person.surname) && Objects.equals(phoneNumber, person.phoneNumber) && Objects.equals(address, person.address) && Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, phoneNumber, address, email);
    }
}