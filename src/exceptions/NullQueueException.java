package exceptions;

public class NullQueueException extends Exception {
    public NullQueueException() {
        super("Given queue is null");
    }

    public NullQueueException(String message) {
        super(message);
    }
}
