package Strategies;

import Controllers.Queue;
import Models.Client;
import exceptions.NullQueueException;

import java.util.List;

public interface Strategy {
    public void addClient(List<Queue> queues, Client t) throws NullQueueException;
}
