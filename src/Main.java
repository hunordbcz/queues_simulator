import Controllers.SimulationManager;
import Util.Constants;
import exceptions.InvalidArgumentsException;
import exceptions.InvalidInputException;
import validators.InputValidator;

import java.io.*;

public class Main {
    public static SimulationManager readFile(String input) throws InvalidArgumentsException, InvalidInputException {
        SimulationManager gen = null;
        File inputFile = new File(input);

        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            String buf;

            int numberOfClients = InputValidator.validateNumberOfClients(Integer.parseInt(br.readLine()));
            int numberOfQueues = InputValidator.validateNumberOfQueues(Integer.parseInt(br.readLine()));
            int simulationInterval = InputValidator.validateSimulationInterval(Integer.parseInt(br.readLine()));

            buf = br.readLine();
            int minArrivalTime = InputValidator.validateMinArrivalTime(Integer.parseInt(buf.substring(0, buf.indexOf(","))));
            int maxArrivalTime = InputValidator.validateMaxArrivalTime(Integer.parseInt(buf.substring(buf.indexOf(",") + 1)));

            buf = br.readLine();
            int minServiceTime = InputValidator.validateMinServiceTime(Integer.parseInt(buf.substring(0, buf.indexOf(","))));
            int maxServiceTime = InputValidator.validateMaxServiceTime(Integer.parseInt(buf.substring(buf.indexOf(",") + 1)));

            gen = new SimulationManager(numberOfClients, numberOfQueues, simulationInterval, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime);
        } catch (IOException ex) {
            throw new InvalidArgumentsException("Input file was not found");
        }

        return gen;
    }

    private static SimulationManager processArguments(String[] args) throws InvalidArgumentsException, InvalidInputException {
        if (args.length < 1) {
            throw new InvalidArgumentsException();
        }

        String input = args[0];
        SimulationManager gen = readFile(input);

        if (args.length > 1) {
            if (args[1].contains("second=")) {
                Constants.ONE_SECOND = InputValidator.validateOneSecond(Integer.parseInt(args[1].substring(args[1].indexOf("=") + 1)));
            } else {
                try {
                    PrintStream fileOut = new PrintStream(args[1]);
                    System.out.println("From now on redirecting output from STDOUT to " + args[1]);
                    System.setOut(fileOut);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (args.length > 2 && args[2].contains("second=")) {
                Constants.ONE_SECOND = InputValidator.validateOneSecond(Integer.parseInt(args[2].substring(args[2].indexOf("=") + 1)));
            }
        }
        return gen;
    }

    public static void main(String[] args) {
        try {
            SimulationManager gen = processArguments(args);
            Thread t = new Thread(gen);
            t.start();
        } catch (InvalidArgumentsException | InvalidInputException e) {
            System.out.println(e.getMessage());
        }

    }
}
