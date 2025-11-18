package classes;

public abstract class Person {

    public String name;
    public String surname;
    public String phoneNumber;
    public Address address;
    public String email;

    public Person(String name, String surname, String phoneNumber, Address address, String email) {
        setName(name);
        setSurname(surname);
        setPhoneNumber(phoneNumber);
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
        this.name =  name;
    }

    public void setSurname(String surname) {
        if (surname == null || surname.isBlank()) {
            throw new IllegalArgumentException("Surname cannot be empty");
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
        this.address = address;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Incorrect email format");
        }
        this.email = email;
    }
}