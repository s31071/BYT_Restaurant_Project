package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {

    @BeforeEach
    void reset() {
        Person.getPersonList().clear();
    }

    @Test
    void testConstructor() {
        Supplier s = new Supplier("Adam", "Nowak", "123456789", "Wrocław", "adam@example.com",
                "BestMeat", Category.meat, 120.0);

        assertEquals("Adam", s.getName());
        assertEquals("BestMeat", s.getCompanyName());
        assertEquals(Category.meat, s.getCategory());
    }

    @Test
    void shouldThrowExceptionForEmptyCompanyName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("Adam", "Nowak", "123456789", "Wrocław", "adam@example.com",
                        "", Category.meat, 120.0));
    }

    @Test
    void shouldThrowExceptionForNullCompanyName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("Adam", "Nowak", "123456789", "Wrocław", "adam@example.com",
                        null, Category.meat, 120.0));
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("Adam", "Nowak", "123456789", "Wrocław", "adam#example.com",
                        "BestMeat", Category.meat, 120.0));
    }
}
