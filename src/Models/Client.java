package Models;

import Util.IDGenerator;

public class Client implements Comparable<Client> {
    private Integer ID;
    private Integer arrivalTime;
    private Integer processingTime;
    private Integer waitingPeriod;

    public Client(int arrivalTime, int processingTime) {
        this.ID = IDGenerator.getClientID();
        this.arrivalTime = arrivalTime;
        this.processingTime = processingTime;
        this.waitingPeriod = 0;
    }

    @Override
    public int compareTo(Client client) {
        if (this.arrivalTime.equals(client.arrivalTime)) {
            return this.processingTime - client.processingTime;
        }
        return this.arrivalTime - client.arrivalTime;
    }

    @Override
    public String toString() {
        return " (" + this.getID() + "," + this.getArrivalTime() + "," + this.getProcessingTime() + ");";
    }

    public void incrementWaitingPeriod() {
        ++waitingPeriod;
    }

    public Integer getID() {
        return ID;
    }

    public Integer getArrivalTime() {
        return arrivalTime;
    }

    public Integer getProcessingTime() {
        return processingTime;
    }

    public Integer decrementProcessingTime() {
        return --processingTime;
    }

    public Integer getWaitingPeriod() {
        return waitingPeriod;
    }
}
