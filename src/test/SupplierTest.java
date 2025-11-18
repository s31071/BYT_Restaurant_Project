package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {
    private Address address;
    @BeforeEach
    void setUp(){
        address = new Address("Markowskiego", "Piaseczno","05-500", "Poland");
    }
    @Test
    void testConstructor() {
        Supplier s = new Supplier("Adam", "Nowak", "123456789", address, "adam@example.com",
                "BestMeat", Category.MEAT, 120.0);

        assertEquals("Adam", s.getName());
        assertEquals("BestMeat", s.getCompanyName());
        assertEquals(Category.MEAT, s.getCategory());
    }

    @Test
    void shouldThrowExceptionForEmptyCompanyName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("Adam", "Nowak", "123456789", address, "adam@example.com",
                        "", Category.MEAT, 120.0));
    }

    @Test
    void shouldThrowExceptionForNullCompanyName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("Adam", "Nowak", "123456789", address, "adam@example.com",
                        null, Category.MEAT, 120.0));
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () ->
                new Supplier("Adam", "Nowak", "123456789", address, "adam#example.com",
                        "BestMeat", Category.MEAT, 120.0));
    }
}
