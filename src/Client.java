public class Client implements Comparable<Client> {
    private Integer ID;
    private Integer arrivalTime;
    private Integer processingTime;

    public Client(int arrivalTime, int processingTime) {
        this.ID = IDGenerator.getClientID();
        this.arrivalTime = arrivalTime;
        this.processingTime = processingTime;
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

    public Integer getFinishTime() {
        return this.arrivalTime + this.processingTime;
    }

    @Override
    public int compareTo(Client client) {
        if (this.arrivalTime.equals(client.arrivalTime)) {
            return this.processingTime - client.processingTime;
        }
        return this.arrivalTime - client.arrivalTime;
    }
}
