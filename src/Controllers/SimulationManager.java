package Controllers;

import Models.Client;
import Util.Constants;
import Util.SelectionPolicy;
import exceptions.QueueStopException;

import java.io.*;
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

    public SimulationManager(String[] args) {
        currentTime = 0;
        generatedClients = generateRandomClients(args);
        SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
        scheduler = new Scheduler(numberOfQueues, selectionPolicy);
    }

    private List<Client> generateRandomClients(String[] args) {
        readFile(args[0]);

        List<Client> clients = new CopyOnWriteArrayList<>();

        if (args.length > 1) {
            if (args[1].contains("second=")) {
                Constants.ONE_SECOND = Integer.parseInt(args[1].substring(args[1].indexOf("=") + 1));
            } else {
                try {
                    PrintStream fileOut = new PrintStream(args[1]);
                    System.out.println("From now on redirecting output from STDOUT to " + args[1]);
                    System.setOut(fileOut);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (args.length > 2) {
            Constants.ONE_SECOND = Integer.parseInt(args[2].substring(args[2].indexOf("=") + 1));
        }

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
        System.out.println("Average waiting time: " + scheduler.averageWaitingTime());

        try {
            scheduler.stop();
        } catch (QueueStopException e) {
            System.out.println(e.getMessage());
        }
    }

    private void readFile(String input) {
        File inputFile = new File(input);

        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            String buf;
            numberOfClients = Integer.parseInt(br.readLine());
            numberOfQueues = Integer.parseInt(br.readLine());
            simulationInterval = Integer.parseInt(br.readLine());
            buf = br.readLine();
            minArrivalTime = Integer.parseInt(buf.substring(0, buf.indexOf(",")));
            maxArrivalTime = Integer.parseInt(buf.substring(buf.indexOf(",") + 1));
            buf = br.readLine();
            minServiceTime = Integer.parseInt(buf.substring(0, buf.indexOf(",")));
            maxServiceTime = Integer.parseInt(buf.substring(buf.indexOf(",") + 1));
        } catch (IOException ex) {
            System.out.println("File " + inputFile + " not found");
        }
    }
}
