package Controllers;

import Models.Client;
import Strategies.ConcreteStrategyQueue;
import Strategies.ConcreteStrategyTime;
import Strategies.Strategy;
import Util.SelectionPolicy;
import exceptions.NullQueueException;
import exceptions.QueueStopException;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;

public class Scheduler {
    private List<Queue> queues;
    private Strategy strategy;
    private Integer maxNoServers;

    public Scheduler(int maxNoServers, SelectionPolicy selectionPolicy) throws InvalidParameterException {
        this.maxNoServers = maxNoServers;
        queues = createServers(maxNoServers);

        switch (selectionPolicy) {
            case SHORTEST_TIME:
                strategy = new ConcreteStrategyTime();
                break;
            case SHORTEST_QUEUE:
                strategy = new ConcreteStrategyQueue();
                break;
            default:
                throw new InvalidParameterException("Invalid selection policy (Scheduler::constructor)");
        }
    }

    private List<Queue> createServers(int nrServers) {
        if (nrServers < 1) {
            throw new InvalidParameterException("nrServers must be greater than 0 (Scheduler::createServers)");
        }

        List<Queue> servers = new LinkedList<>();

        for (int i = 0; i < nrServers; i++) {
            Queue queue = new Queue();
            servers.add(queue);
        }

        return servers;
    }

    public void dispatchClient(Client client) {
        try {
            strategy.addClient(queues, client);
        } catch (NullQueueException e) {
            queues = createServers(maxNoServers);
            dispatchClient(client);
        }
    }

    public Boolean queuesEmpty() {
        for (Queue queue : queues) {
            if (!queue.getClients().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public Boolean stop() throws QueueStopException {
        for (Queue queue : queues) {
            if (queue.isRunning()) {
                if (!queue.stop()) {
                    throw new QueueStopException();
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        for (Queue queue : queues) {
            response.append(queue);
        }
        return response.toString();
    }

    public double averageWaitingTime() {
        double totalWaitingTime = 0;
        int totalProcessed = 0;
        for (Queue queue : queues) {
            totalWaitingTime += (double) queue.getTotalWaitingPeriod();
            totalProcessed += queue.getTotalProcessed();
        }
        if (totalProcessed == 0) {
            return 0;
        }
        return (totalWaitingTime / totalProcessed);
    }
}
