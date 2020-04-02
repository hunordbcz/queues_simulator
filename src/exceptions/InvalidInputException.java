package exceptions;

public class InvalidInputException extends Exception {
    public InvalidInputException() {
        super("Invalid input entered");
    }

    public InvalidInputException(String message) {
        super(message);
    }
}
