package exceptions;

public class InvalidArgumentsException extends Exception {
    public InvalidArgumentsException() {
        super("Invalid arguments | usage: <input> ?<output> ?<second=>");
    }

    public InvalidArgumentsException(String message) {
        super(message);
    }
}
