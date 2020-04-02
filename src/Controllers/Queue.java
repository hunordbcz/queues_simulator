package Controllers;

import Models.Client;
import Util.Constants;
import Util.IDGenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue implements Runnable {
    private Integer ID;
    private AtomicBoolean running;
    private BlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod;
    private AtomicInteger totalWaitingPeriod;
    private AtomicInteger totalProcessed;

    public Queue() {
        this.ID = IDGenerator.getQueueID();
        this.clients = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
        this.totalWaitingPeriod = new AtomicInteger(0);
        this.running = new AtomicBoolean(false);
        this.totalProcessed = new AtomicInteger(0);
    }

    public synchronized void addClient(Client client) {
        if (!this.isRunning()) {
            start();
        }
        clients.add(client);
        waitingPeriod.addAndGet(client.getProcessingTime());
    }

    @Override
    public void run() {
        while (this.isRunning()) {
            try {
                if (clients.isEmpty()) {
                    continue;
                }

                Client currentClient = clients.peek();

                Thread.sleep(Constants.ONE_SECOND);
                waitingPeriod.decrementAndGet();
                this.incrementClientsWaitingPeriod();

                if (currentClient.decrementProcessingTime() == 0) {
                    totalWaitingPeriod.addAndGet(currentClient.getWaitingPeriod());
                    totalProcessed.incrementAndGet();
                    clients.take();
                }
            } catch (InterruptedException e) {
                System.out.println("Controllers.Queue " + this.getID() + "interrupted");
                return;
            }
        }
    }

    public Boolean isRunning() {
        return running.get();
    }

    public void start() {
        running.set(true);
        Thread t = new Thread(this);
        t.start();
    }

    public Boolean stop() {
        running.set(false);
        return true;
    }

    private void incrementClientsWaitingPeriod() {
        for (Client client : clients) {
            client.incrementWaitingPeriod();
        }
    }

    public List<Client> getClients() {
        return new LinkedList<>(this.clients);
    }

    public Integer getID() {
        return this.ID;
    }

    public Integer getWaitingPeriod() {
        return this.waitingPeriod.get();
    }

    public Integer getTotalWaitingPeriod() {
        return totalWaitingPeriod.get();
    }

    public int getTotalProcessed() {
        return totalProcessed.get();
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder("Queue " + getID() + ":");
        List<Client> clients = getClients();

        if (clients.isEmpty()) {
            response.append(" closed");
        }

        for (Client client : clients) {
            response.append(client);
        }

        response.append("\n");
        return response.toString();
    }
}
