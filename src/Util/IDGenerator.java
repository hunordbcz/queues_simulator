package Util;

public class IDGenerator {

    private static int queueID = Constants.IDs_STARTS_FROM;
    private static int clientID = Constants.IDs_STARTS_FROM;

    private IDGenerator() {

    }

    public static int getQueueID() {
        return queueID++;
    }

    public static int getClientID() {
        return clientID++;
    }
}
