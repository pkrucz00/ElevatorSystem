package pkrucz00;

import pkrucz00.Simulation.Scenario;
import pkrucz00.Simulation.ScenarioInfo;

public class Main {

    public static void main(String args[]){
        if (args.length != 6 && args.length != 1){
            throw new IllegalArgumentException("Wrong number of arguments (Should be 1 or 6, was " + args.length + ")");
        }
        if (args.length == 1){
            if (args[0].equals("help")){
                printHelp();
            } else
                throw new IllegalArgumentException("Unrocognized parameter: " + args[0]);
        }else {

            Scenario scenario = new Scenario(new ScenarioInfo(args));
            try {
                scenario.runScenario();
                System.out.println("Simulation ended. No more iterations");
            } catch (InterruptedException e) {
                System.out.println("Simulation ended by interruption");
            }
        }
    }

    public static void printHelp(){
        System.out.println("*** ELEVATOR SYSTEM ***");
        System.out.println("Author: Paweł Kruczkiewicz");
        System.out.println("To run the program, simply type:\n");

        System.out.println("\tjava pkrucz00/Main [noElevators] [noStoreys] [noIterations] [reversedIntensity] [sleepTime] [mode]\n");

        System.out.println("where:");
        System.out.println("\t");
        System.out.println( "`noElevators` - number of elevators in system);");
        System.out.println( "`noStoreys` - number of floors in our building (excluding the ground floor");
        System.out.println( "`noIterations` - number of iterations (steps) our elevator system will proceed in the simulation");
        System.out.println( "`reversedIntensity` - integer indicating expected length of a time interval (in steps) between spawns of a person in a building");
        System.out.println( "`sleepTime` - [integer] time between steps (in seconds)");
        System.out.println("`mode` - one of 3 modes in the simulation. They include:");
        System.out.println("\t- `uniform` - uniform distribution of people in equal time intervals");
        System.out.println("\t- `bursts` - similar to `uniform` but 2-6 people spawn at once)");
        System.out.println("\t- `irregurarly` - similar to `uniform` but the time interval between two spawns is irregular");

    }
}
