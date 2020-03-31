package Controllers;

import Models.Client;
import Util.Constants;
import Util.SelectionPolicy;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimulationManager implements Runnable {

    public int numberOfClients;
    public int numberOfQueues;
    public int simulationInterval;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int minServiceTime;
    public int maxServiceTime;

    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;

    private int currentTime;
    private Scheduler scheduler;
    private List<Client> generatedClients;

    public SimulationManager(String[] args) {
        currentTime = 0;
        generatedClients = new CopyOnWriteArrayList<>();
        generateRandomClients(args);
        scheduler = new Scheduler(numberOfQueues, numberOfClients);
    }

    private void generateRandomClients(String[] args) {
        readFile(args[0]);

        if (args.length > 1) {
            try {
                PrintStream fileOut = new PrintStream(args[1]);
                System.setOut(fileOut);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        Random random = new Random();
        for (int i = 1; i <= numberOfClients; i++) {
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int serviceTime = random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
            Client client = new Client(arrivalTime, serviceTime);
            generatedClients.add(client);
        }
//        generatedClients.add(new Models.Client(2,2));    // Tests from PDF
//        generatedClients.add(new Models.Client(3,3));
//        generatedClients.add(new Models.Client(4,3));
//        generatedClients.add(new Models.Client(10,2));
        generatedClients.sort(Client::compareTo);
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
        scheduler.stop();
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
