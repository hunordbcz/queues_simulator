import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable {

    public int numberOfClients;
    public int numberOfQueues;
    public int simulationInterval;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int minServiceTime;
    public int maxServiceTime;

    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;

    private Scheduler scheduler;
    private List<Client> generatedClients;

    public SimulationManager(String[] args) {
        generatedClients = new LinkedList<>();
        generateRandomClients(args);
        scheduler = new Scheduler(numberOfClients, numberOfClients);
    }

    public static void main(String[] args) {
        SimulationManager gen = new SimulationManager(args);
        Thread t = new Thread(gen);
        t.start();
    }

    private void generateRandomClients(String[] args) {
        readFile(args[0]);
        Random random = new Random();
        for (int i = 1; i <= numberOfClients; i++) {
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int serviceTime = random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
            Client client = new Client(arrivalTime, serviceTime);
            generatedClients.add(client);
        }
        generatedClients.sort(Client::compareTo);
    }

    @Override
    public void run() {
        int currentTime = 0;
        while (currentTime < simulationInterval) {
            for (Client client : generatedClients) {
                if (client.getArrivalTime() > simulationInterval) {
                    break;
                }
                scheduler.dispatchClient(client);
                generatedClients.remove(client);
            }
            currentTime++;
        }
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
