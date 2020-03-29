import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue implements Runnable {
    private Integer ID;
    private Boolean running = false;
    private BlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod;

    public Queue(int maxClients) {
        this.ID = IDGenerator.getQueueID();
        this.clients = new ArrayBlockingQueue<>(maxClients);
        this.waitingPeriod = new AtomicInteger(0);
    }

    public void addClient(Client client) {
        if (!running) {
            start();
        }
        clients.add(client);
        waitingPeriod.addAndGet(client.getProcessingTime());
    }

    @Override
    public void run() {
        while (true) {
            try {
                Client client = clients.take();
                Thread.sleep(client.getProcessingTime());
                System.out.println("asd");
                waitingPeriod.addAndGet(-client.getProcessingTime());
            } catch (InterruptedException e) {
                System.out.println("Queue interrupted");
                return;
            }
        }
    }

    public List<Client> getClients() {
        return new LinkedList<>(this.clients);
    }

    public Boolean isRunning() {
        return running;
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
        running = true;
    }

    public void stop() {
        Thread.currentThread().stop();
        running = false;
    }
}
