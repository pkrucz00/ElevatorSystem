package pkrucz00.mainClasses;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

import static java.lang.Math.abs;

public class ElevatorSystem implements IElevatorSystem{
    private final int noElevators;
    Elevator[] elevators;

    public ElevatorSystem(int noElevators, int initialFloor){
        this.noElevators = noElevators;
        this.elevators = new Elevator[noElevators];
        for (int i = 0; i < noElevators; i++){
            elevators[i] = new Elevator(i, initialFloor);
        }
        if (noElevators < 0){
            throw new IllegalArgumentException("Elevator system must have at least 1 elevator ("
                    + noElevators + " were given)");
        }
    }

    /**
     * This implementation of pickup works as follows:
     *      At first we try to find the closest elevator (regarding the current floor) from group
     *      where all the elevators:
     *          - are in IDLE
     *          - are in concurring state (that is UP if the request is up or down otherwise) AND
     *                  the requested floor is in range between current and destination floor of the elevator
     *      If there is no elevator in such group, we take the closest elevator (regarding the destination floor)
     *
     * @param floorNumber  - floor on which a person or people is/are waiting for the elevator
     * @param isGoingUp     - indicator of whether the person would like to go up or down
     * @return elevator to which the request was sent
     */
    @Override
    public Elevator pickup(int floorNumber, boolean isGoingUp) {
        ElevatorState concurringState = isGoingUp ? ElevatorState.UP : ElevatorState.DOWN;
        int bestElevatorID;

        Optional<StatusQuadruple> bestCandidate = Arrays.stream(this.status())
                .filter((status) -> status.state == ElevatorState.IDLE ||
                                    (status.state == concurringState &&
                                    inRange(floorNumber, status.currFloor, status.destFloor)))
                .min(Comparator.comparingInt((StatusQuadruple e) -> abs(e.currFloor - floorNumber)));

        if (bestCandidate.isPresent()) {
            bestElevatorID = bestCandidate.get().elevatorID;
        } else {
            Optional<StatusQuadruple> secondBestCandidate = Arrays.stream(this.status())
                    .min(Comparator.comparingInt((StatusQuadruple e) -> abs(e.destFloor - floorNumber)));
            if (secondBestCandidate.isPresent())
                bestElevatorID = secondBestCandidate.get().elevatorID;
            else
                throw new AssertionError("Error! No elevators matching the pickup request");
        }

        Elevator bestElevator = this.elevators[bestElevatorID];
        bestElevator.addJob(floorNumber);

        return bestElevator;
    }


    private boolean inRange(int x, int curr, int dest){
        return (curr < x && x <= dest) || (dest <= x && x < curr);
    }

    @Override
    public void step() {
        for (Elevator elevator:elevators){
            elevator.step();
        }
    }

    @Override
    public StatusQuadruple[] status() {
        StatusQuadruple[] statuses = new StatusQuadruple[noElevators];
        for (int i = 0; i < noElevators; i++){
            Elevator currElev = this.elevators[i];
            statuses[i] = new StatusQuadruple(currElev.getElevatorID(),
                                           currElev.getCurrentFloor(),
                                           currElev.getDestinationFloor(),
                                           currElev.getCurrentState());
        }

        return statuses;
    }

    public Elevator[] getElevators() {
        return elevators;
    }

    public int getNoPeopleInElevators() { return Arrays.stream(this.elevators).
            mapToInt(Elevator::getNoPeopleInside).
            reduce(0, Integer::sum);}
}
