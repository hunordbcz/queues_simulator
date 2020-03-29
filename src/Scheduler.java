import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Queue> queues;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        queues = new ArrayList<>();
        strategy = new ConcreteStrategyTime();

        for (int i = 0; i < maxNoServers; i++) {
            Queue queue = new Queue(maxTasksPerServer);
            queues.add(queue);
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ConcreteStrategyTime();
        }
    }

    public void dispatchClient(Client client) {
        strategy.addClient(queues, client);
    }

    public List<Queue> getQueues() {
        return queues;
    }

    public void stop() {
        for (Queue queue : queues) {
            if (queue.isRunning()) {
                queue.stop();
            }
        }
    }
}
