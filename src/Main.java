import Controllers.SimulationManager;

public class Main {
    public static void main(String[] args) {
        SimulationManager gen = new SimulationManager(args);
        Thread t = new Thread(gen);
        t.start();
    }
}
