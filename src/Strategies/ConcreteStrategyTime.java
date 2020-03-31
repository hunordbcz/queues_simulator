package Strategies;

import Controllers.Queue;
import Models.Client;

import java.security.InvalidParameterException;
import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    @Override
    public void addClient(List<Queue> queues, Client client) {
        Queue bestQueue = null;
        Integer maxWait = -1;
        for (Queue queue : queues) {
            if (maxWait == -1 || maxWait > queue.getWaitingPeriod()) {
                maxWait = queue.getWaitingPeriod();
                bestQueue = queue;
            }
        }

        if (bestQueue == null) {
            throw new InvalidParameterException(); // Do some normal exception here
        }
        bestQueue.addClient(client);
    }
}
