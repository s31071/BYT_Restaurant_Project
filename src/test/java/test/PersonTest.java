package test.java.test;

import classes.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    static class TestPerson extends Person {
        public TestPerson(String name, String surname, String phoneNumber, String address, String email) {
            super(name, surname, phoneNumber, address, email);
        }
    }

    private TestPerson person1;
    private TestPerson person2;

    @BeforeEach
    void setUp() {
        Person.getPersonList().clear();
        person1 = new TestPerson("Jan", "Kowalski", "123456789", "Warszawa", "jan@example.com");
        person2 = new TestPerson("Anna", "Nowak", "987654321", "Kraków", "anna@example.com");
    }

    @Test
    void testConstructor() {
        assertEquals("Jan", person1.getName());
        assertEquals("Kowalski", person1.getSurname());
        assertEquals("123456789", person1.getPhoneNumber());
        assertEquals("Warszawa", person1.getAddress());
        assertEquals("jan@example.com", person1.getEmail());
    }

    @Test
    void testAddPersonList() {
        List<Person> list = Person.getPersonList();
        assertEquals(2, list.size());
        assertTrue(list.contains(person1));
        assertTrue(list.contains(person2));
    }

    @Test
    void shouldThrowExceptionForBlankName() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestPerson("", "Nowak", "123456789", "Warszawa", "jan@example.com"));
    }

    @Test
    void shouldThrowExceptionForBlankSurname() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestPerson("Jan", " ", "123456789", "Warszawa", "jan@example.com"));
    }

    @Test
    void shouldThrowExceptionForInvalidPhone() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestPerson("Jan", "Kowalski", "12345", "Warszawa", "jan@example.com"));
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestPerson("Jan", "Kowalski", "123456789", "Warszawa", "invalid_email"));
    }

    @Test
    void shouldThrowExceptionForNullPersonAdd() throws Exception {
        var method = Person.class.getDeclaredMethod("addPerson", Person.class);
        method.setAccessible(true);

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, (Object) null));

        assertTrue(ex.getTargetException() instanceof IllegalArgumentException);
        assertEquals("Person cannot be null", ex.getTargetException().getMessage());
    }

}
