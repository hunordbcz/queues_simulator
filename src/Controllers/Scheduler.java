package Controllers;

import Models.Client;
import Strategies.ConcreteStrategyTime;
import Strategies.Strategy;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Queue> queues;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;
    private int totalDispatched;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        queues = new ArrayList<>();
        strategy = new ConcreteStrategyTime();
        this.maxNoServers = maxNoServers;
        totalDispatched = 0;

        for (int i = 0; i < maxNoServers; i++) {
            Queue queue = new Queue();
            queues.add(queue);
        }
    }

    public void dispatchClient(Client client) {
        strategy.addClient(queues, client);
        totalDispatched++;
    }

//    public List<Controllers.Queue> getQueues() {
//        return queues;
//    }

    public Boolean queuesEmpty() {
        for (Queue queue : queues) {
            if (!queue.getClients().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void stop() {
        for (Queue queue : queues) {
            if (queue.isRunning()) {
                queue.stop();
            }
        }
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
        for (Queue queue : queues) {
            totalWaitingTime += (double) queue.getTotalWaitingPeriod();
        }
        return (totalWaitingTime / totalDispatched);
    }
}
