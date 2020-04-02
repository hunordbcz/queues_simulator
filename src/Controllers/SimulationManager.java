package Controllers;

import Models.Client;
import Util.Constants;
import Util.SelectionPolicy;
import exceptions.QueueStopException;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimulationManager implements Runnable {
    private int numberOfClients;
    private int numberOfQueues;
    private int simulationInterval;
    private int minArrivalTime;
    private int maxArrivalTime;
    private int minServiceTime;
    private int maxServiceTime;

    private int currentTime;
    private Scheduler scheduler;
    private List<Client> generatedClients;

    public SimulationManager(int numberOfClients, int numberOfQueues, int simulationInterval, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime) {
        this.numberOfClients = numberOfClients;
        this.numberOfQueues = numberOfQueues;
        this.simulationInterval = simulationInterval;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;

        currentTime = 0;
        generatedClients = generateRandomClients();
        SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
        scheduler = new Scheduler(this.numberOfQueues, selectionPolicy);
    }

    private List<Client> generateRandomClients() {
        List<Client> clients = new CopyOnWriteArrayList<>();

        Random random = new Random();
        for (int i = 1; i <= numberOfClients; i++) {
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int serviceTime = random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
            Client client = new Client(arrivalTime, serviceTime);
            clients.add(client);
        }
        clients.sort(Client::compareTo);
        return clients;
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder("Time " + currentTime + "\nWaiting clients:");

        if (generatedClients.isEmpty()) {
            response.append("No one");
        } else {
            for (Client client : generatedClients) {
                response.append(client);
            }
        }
        response.append("\n").append(scheduler);
        return response.toString();
    }

    @Override
    public void run() {
        while (currentTime < simulationInterval && (!generatedClients.isEmpty() || !scheduler.queuesEmpty())) {
            for (Client client : generatedClients) {
                if (client.getArrivalTime() > currentTime) {
                    break;
                }

                scheduler.dispatchClient(client);
                generatedClients.remove(client);
            }
            System.out.println(this);
            try {
                Thread.sleep(Constants.ONE_SECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTime++;
        }
        System.out.println(this);
        System.out.println("Average waiting time: " + scheduler.getAverageWaitingTime());

        try {
            scheduler.stop();
        } catch (QueueStopException e) {
            System.out.println(e.getMessage());
        }
    }
}
