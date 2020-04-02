package exceptions;

public class QueueStopException extends Exception {
    public QueueStopException() {
        super("Queue couldn't be stopped");
    }

    public QueueStopException(String message) {
        super(message);
    }
}