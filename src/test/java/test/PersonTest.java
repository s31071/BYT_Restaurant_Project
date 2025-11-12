package test.java.test;

import classes.Address;
import classes.Person;
import classes.ProductOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    @BeforeEach
    void setup() {
        Person person = new Person("Mark", "Marks", "123999222", "Some Street", "Mark@Marks.com");
    }

    @Test
    void validateName_test(){
        String name = "Mark";
        assertEquals();
    }
}
