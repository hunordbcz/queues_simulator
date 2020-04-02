package validators;

import exceptions.InvalidArgumentsException;
import exceptions.InvalidInputException;

public class InputValidator {
    private static int minArrivalTime;
    private static int minServiceTime;

    private InputValidator() {

    }

    public static int validateNumberOfClients(int val) throws InvalidInputException {
        if (val < 1) {
            throw new InvalidInputException("Number of Clients must be greater than 0");
        }
        return val;
    }

    public static int validateNumberOfQueues(int val) throws InvalidInputException {
        if (val < 1) {
            throw new InvalidInputException("Number of Queues must be greater than 0");
        }
        return val;
    }

    public static int validateSimulationInterval(int val) throws InvalidInputException {
        if (val < 1) {
            throw new InvalidInputException("Simulation Interval must be greater than 0");
        }
        return val;
    }

    public static int validateMinArrivalTime(int val) throws InvalidInputException {
        if (val < 1) {
            throw new InvalidInputException("Minimum Arrival Time must be greater than 0");
        }
        return (minArrivalTime = val);
    }


    public static int validateMaxArrivalTime(int val) throws InvalidInputException {
        if (val < minArrivalTime) {
            throw new InvalidInputException("Maximum Arrival Time must be greater than Minimum Arrival Time");
        }
        return val;
    }


    public static int validateMinServiceTime(int val) throws InvalidInputException {
        if (val < 1) {
            throw new InvalidInputException("Minimum Service Time must be greater than 0");
        }
        return (minServiceTime = val);
    }


    public static int validateMaxServiceTime(int val) throws InvalidInputException {
        if (val < minServiceTime) {
            throw new InvalidInputException("Maximum Service Time must be greater than Minimum Service Time");
        }
        return val;
    }

    public static int validateOneSecond(int val) throws InvalidArgumentsException {
        if (val <= 0) {
            throw new InvalidArgumentsException("Second must be greater than 0");
        }
        return val;
    }
}
