package pkrucz00.Simulation;

import pkrucz00.Auxiliary.Building;
import pkrucz00.Auxiliary.Person;
import pkrucz00.mainClasses.ElevatorSystem;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Scenario {
    private final ScenarioInfo info;

    private int cumulativePeopleCounter = 0;

    public Scenario(ScenarioInfo info){
        this.info = info;
        if (info.noStoreys <= 0){
            throw new IllegalArgumentException("Warning! The building has to have at least one floor");
        }
    }

    public void runScenario() throws InterruptedException {
        ElevatorSystem elevSys = new ElevatorSystem(info.noElevators, 0);
        Building building = new Building(info.noStoreys, elevSys);


        for (int i = 0; i < info.noIterations; i++){
            this.dispensePeople(building, i);

            int noPeopleWaiting = building.getNoPeopleInBuilding() - elevSys.getNoPeopleInElevators();
            System.out.println("PEOPLE WAITING: " + noPeopleWaiting);
            System.out.println("ITERATION: " + i);
            System.out.println(building);

            elevSys.step();
            building.makePeopleObserveTheElevator();
            sleep(info.sleep*1000);
        }

    }

    private void generatePeople(int noPeople, Building building){
        Random random = new Random();
        int upperBound = building.getMaxFloorNumber() + 1;  // exclusive
        int initFloor = random.nextInt(upperBound);
        int destFloor = random.nextInt(upperBound);
        while (initFloor == destFloor){
            destFloor = random.nextInt(upperBound);
        }

        for (int i = 0; i < noPeople; i++) {
            Person newPerson = new Person(cumulativePeopleCounter++, initFloor, destFloor, building);
            newPerson.sendRequestForElevator();
        }
    }

    private void dispensePeople(Building building, int iteration){
        int noPeople = info.scenario == ScenarioType.BURSTS ?
                around(4, 2) : 1;

        boolean shouldGeneratePeople = shouldGenerate(iteration);
        if (shouldGeneratePeople){
            this.generatePeople(noPeople, building);
        }
    }

    private boolean shouldGenerate(int iteration){
        if (info.scenario == ScenarioType.IRREGULARLY) {
            int expectedProbability = (int) (1 / (double) info.reversedIntensity);
            int maxRange = expectedProbability/2;  //the probability varies at most by half
            int tamperedProbability = around(expectedProbability, maxRange);
            return trueWithProbability(tamperedProbability);
        } else {
            return (iteration % info.reversedIntensity)  == 0;
        }
    }

    private int around(int expectedValue, int maxRange){
        //maxRange - biggest distance between expected number and result
        Random random = new Random();
        return random.nextInt(2*maxRange + 1)  + expectedValue - maxRange;
    }


    private boolean trueWithProbability(int probability){
        Random random = new Random();
        int randomIntFrom1To100 = random.nextInt(100) + 1;
        return randomIntFrom1To100 > probability;
    }
}
