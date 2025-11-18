package classes;

//potrzebujemy klasy address, żeby mieć complex attribute
//data też jest complex, ale Java ma wbudowaną funkcje, a dla adresu nie

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class Address implements Serializable {

    private static List<Address> extent = new ArrayList<>();

    private String street;
    private String city;
    private String postalCode;
    private String country;

    public Address(String street, String city, String postalCode, String country) {
        setStreet(street);
        setCity(city);
        setPostalCode(postalCode);
        setCountry(country);
        addExtent(this);
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        if (street == null || street.isEmpty()) {
            throw new IllegalArgumentException("Street cannot be empty");
        }
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty");
        }
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        if (postalCode == null || postalCode.isEmpty()) {
            throw new IllegalArgumentException("Postal code cannot be empty");
        }
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        if (country == null || country.isEmpty()) {
            throw new IllegalArgumentException("Country cannot be empty");
        }
        this.country = country;
    }

    public static void addExtent(Address newAddress) {
        if (newAddress == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }

        for (Address existingAddress : extent) {
            boolean sameStreet = existingAddress.street.equals(newAddress.street);
            boolean sameCity = existingAddress.city.equals(newAddress.city);
            boolean samePostal = existingAddress.postalCode.equals(newAddress.postalCode);
            boolean sameCountry = existingAddress.country.equals(newAddress.country);

            if (sameStreet && sameCity && samePostal && sameCountry) {
                throw new IllegalArgumentException("This Address already exists in extent");
            }
        }

        extent.add(newAddress);
    }

    public static List<Address> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void removeFromExtent(Address address) {
        extent.remove(address);
    }

    public static void writeExtent(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(extent);
    }

    public static void readExtent(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        extent = (List<Address>) objectInputStream.readObject();
    }
}