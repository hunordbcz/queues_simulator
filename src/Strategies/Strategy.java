package Strategies;

import Controllers.Queue;
import Models.Client;

import java.util.List;

public interface Strategy {
    public void addClient(List<Queue> queues, Client t);
}
