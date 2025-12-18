package test;

import classes.Address;
import classes.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    static class TestPerson extends Person {
        public TestPerson(String name, String surname, String phoneNumber,
                          String street, String city, String postalCode,
                          String country, String email) {
            super(name, surname, phoneNumber, street, city, postalCode, country, email);
        }
    }

    private TestPerson person1;
    private TestPerson person2;
    private Address address;

    @BeforeEach
    void setUp() {
        Person.clearExtent();

        person1 = new TestPerson(
                "Jan", "Kowalski", "123456789",
                "Markowskiego", "Piaseczno", "05-500",
                "Poland", "jankowalski@gmail.com"
        );

        person2 = new TestPerson(
                "Anna", "Nowak", "987654321",
                "Markowskiego", "Piaseczno", "05-500",
                "Poland", "annanowak@gmail.com"
        );

        address = new Address("Markowskiego", "Piaseczno", "05-500", "Poland");
    }

    @Test
    void testConstructor() {
        assertEquals("Jan", person1.getName());
        assertEquals("Kowalski", person1.getSurname());
        assertEquals("123456789", person1.getPhoneNumber());
        assertEquals(address, person1.getAddress());
        assertEquals("jankowalski@gmail.com", person1.getEmail());
    }

    @Test
    void shouldThrowExceptionForBlankName() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestPerson(
                        "", "Nowak", "123456789",
                        "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "jan@gmail.com"
                ));
    }

    @Test
    void shouldThrowExceptionForNameWithInvalidCharacters() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestPerson(
                        "Jan3", "Nowak", "123456789",
                        "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "jan@gmail.com"
                ));
    }

    @Test
    void shouldThrowExceptionForNameWithSymbols() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestPerson(
                        "J@n", "Nowak", "123456789",
                        "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "jan@gmail.com"
                ));
    }

    @Test
    void shouldThrowExceptionForBlankSurname() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestPerson(
                        "Jan", " ", "123456789",
                        "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "jan@gmail.com"
                ));
    }

    @Test
    void shouldThrowExceptionForSurnameWithInvalidCharacters() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestPerson(
                        "Jan", "Kowal$ki", "123456789",
                        "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "jan@gmail.com"
                ));
    }

    @Test
    void shouldThrowExceptionForSurnameContainingDigits() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestPerson(
                        "Jan", "Kowal5ki", "123456789",
                        "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "jan@gmail.com"
                ));
    }

    @Test
    void shouldThrowExceptionForInvalidPhone() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestPerson(
                        "Jan", "Kowalski", "12345",
                        "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "jan@gmail.com"
                ));
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestPerson(
                        "Jan", "Kowalski", "123456789",
                        "Markowskiego", "Piaseczno", "05-500",
                        "Poland", "invalid_email"
                ));
    }
}