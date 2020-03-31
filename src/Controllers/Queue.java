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

    public Queue() {
        this.ID = IDGenerator.getQueueID();
        this.clients = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
        this.totalWaitingPeriod = new AtomicInteger(0);
        this.running = new AtomicBoolean(false);
    }

    public synchronized void addClient(Client client) {
        if (!running.get()) {
            start();
        }
        clients.add(client);
        waitingPeriod.addAndGet(client.getProcessingTime());
    }

    @Override
    public void run() {
        while (this.isRunning()) {
            try {
                Thread.sleep(Constants.ONE_SECOND);
                totalWaitingPeriod.addAndGet(clients.size());
                if (!clients.isEmpty()) {
                    clients.peek().decrementProcessingTime();
                    waitingPeriod.decrementAndGet();
                }
                if (!clients.isEmpty() && clients.peek().getProcessingTime() == 0) {
                    clients.take();
                }
            } catch (InterruptedException e) {
                System.out.println("Controllers.Queue " + this.getID() + "interrupted");
                return;
            }
        }
    }

    public List<Client> getClients() {
        return new LinkedList<>(this.clients);
    }

    public Boolean isRunning() {
        return running.get();
    }

    public void start() {
        running.set(true);
        Thread t = new Thread(this);
        t.start();
    }

    public void stop() {
        running.set(false);
    }

    public Integer getID() {
        return this.ID;
    }

    public Integer getWaitingPeriod() {
        return this.waitingPeriod.get();
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder("Controllers.Queue " + getID() + ":");
        List<Client> clients = getClients();

        if (clients.isEmpty()) {
            response.append("closed");
        }

        for (Client client : clients) {
            response.append(client);
        }

        response.append("\n");
        return response.toString();
    }

    public Integer getTotalWaitingPeriod() {
        return totalWaitingPeriod.get();
    }
}
