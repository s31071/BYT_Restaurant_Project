package classes;

//potrzebujemy klasy address, żeby mieć complex attribute
//data też jest complex, ale Java ma wbudowaną funkcje, a dla adresu nie

import java.io.Serializable;

public class Address implements Serializable {

    private String street;
    private String city;
    private String postalCode;
    private String country;


    public Address(String street, String city, String postalCode, String country) {
        setStreet(street);
        setCity(city);
        setPostalCode(postalCode);
        setCountry(country);
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
}