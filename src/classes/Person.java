package classes;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Person {
    private static List<Person> extent = new ArrayList<>();

    private String name;
    private String surname;
    private String phoneNumber;
    private Address address;
    private String email;

    private Employee employee;
    private Customer customer;

    public Person(){}
    public Person(String name, String surname, String phoneNumber, String street, String city, String postalCode, String country, String email) {
        setName(name);
        setSurname(surname);
        setPhoneNumber(phoneNumber);
        Address address = new Address(street, city, postalCode, country);
        setAddress(address);
        setEmail(email);

        addExtent(this);
    }

    public void becomeEmployee(Employee employee) throws Exception { //TODO: Wydaje mi sie ze nie musi byc bidirectional bo employee bez person nie istnieje wiec w druga strone employee musi miec w konstruktorze person
        if (employee == null) {
            throw new Exception("Employee cannot be null");
        }

        if (this.employee == null) {
            this.employee = employee;
            if (employee.getPerson() != this) {
                employee.setPerson(this);
            }
        } else if (this.employee != employee) {
            throw new IllegalStateException("This person already is an employee");
        }
    }

    public void becomeCustomer(Customer customer) throws Exception {
        if (customer == null) {
            throw new Exception("Customer cannot be null");
        }

        if (this.customer == null) {
            this.customer = customer;
            if (customer.getPerson() != this) {
                customer.setPerson(this);
            }
        } else if (this.customer != customer) {
            throw new IllegalStateException("This person already is an customer");
        }
    }

    public void stopBeingEmployee(Employee employee) throws Exception {
        if (employee == null) {
            throw new Exception("Employee cannot be null");
        }

        if (this.employee != employee) {
            return;
        }

        this.employee = null;

        Employee.removeFromExtent(employee);
    }

    public void stopBeingCustomer(Customer customer) throws Exception {
        if (customer == null) {
            throw new Exception("Customer cannot be null");
        }

        if (this.customer != customer) {
            return;
        }

        this.customer = null;

        Customer.removeFromExtent(customer);
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

    public void setEmployee(Employee employee) throws Exception {
        if(employee == null){
            throw new Exception("Employee cannot be null");
        }
        this.employee = employee;
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

    public Employee getEmployee() {
        return employee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public static void addExtent(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        if (extent.contains(person)) {
            throw new IllegalArgumentException("Such person is already in data base");
        }
        extent.add(person);
    }

    public static List<Person> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Person person) throws Exception {
        person.stopBeingCustomer(person.getCustomer());
        person.stopBeingEmployee(person.getEmployee());
        extent.remove(person);
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static void writeExtent(XMLEncoder out) throws IOException {
        out.writeObject(extent);
    }

    public static void readExtent(XMLDecoder in) throws IOException, ClassNotFoundException {
        extent = (List<Person>) in.readObject();
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