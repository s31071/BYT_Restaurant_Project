package classes;

import java.util.ArrayList;
import java.util.List;

public abstract class Person {
    private static List<Person> personList = new ArrayList<>();

    private String name;
    private String surname;
    private String phoneNumber;
    private String address;
    private String email;

    public Person(String name, String surname, String phoneNumber, String address, String email) {
        this.name = validateName(name);
        this.surname = validateSurname(surname);
        this.phoneNumber = validatePhoneNumber(phoneNumber);
        this.address = address;
        this.email = validateEmail(email);

        addPerson(this);
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return name;
    }

    private String validateSurname(String surname) {
        if (surname == null || surname.isBlank()) {
            throw new IllegalArgumentException("Surname cannot be empty");
        }
        return surname;
    }

    private String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("\\d{9}")) {
            throw new IllegalArgumentException("Telephone number has to contains 9 digits");
        }
        return phoneNumber;
    }

    private String validateEmail(String email) {
        if (email == null || !email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Incorrect email format");
        }
        return email;
    }

    private static void addPerson(Person person){
        if(person == null){
            throw new IllegalArgumentException("Person cannot be null");
        }

        personList.add(person);
    }

    public static List<Person> getPersonList() {
        return personList;
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

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }
}