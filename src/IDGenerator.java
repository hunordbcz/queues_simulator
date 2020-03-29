public class IDGenerator {

    private static int queueID = 1;
    private static int clientID = 1;

    private IDGenerator() {

    }

    public static int getQueueID() {
        return queueID++;
    }

    public static int getClientID() {
        return clientID++;
    }
}
