package classes;

public class IllegalIdException extends Throwable {
    public IllegalIdException(String invalidId) {
        super("Invalid ID: " + invalidId);
    }
}
