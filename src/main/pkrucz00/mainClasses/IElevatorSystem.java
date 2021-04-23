package pkrucz00.mainClasses;

/**
 *  IElevatorSystem controls elevators, receives signals and sends orders to elevators.
 *  Moreover it can show the current status of the elevators
 **/
public interface IElevatorSystem {
    /**
     * Chooses the elevator that should take the given task
     *
     * @param floorNumber  - floor on which a person or people is/are waiting for the elevator
     * @param isGoingUp     - indicator of whether the person would like to go up or down
     * @return Elevator that was chosen to go for this order
     */
    Elevator pickup(int floorNumber, boolean isGoingUp);

    /**
     * Tells all the elevators to make a step
     */
    void step();

    /**
     * Returns the current state of the system
     * @return StatusQuadruple table consisting of quadruples in form
     * `(elevatorID, currentFloor, destinationFloor, state)`, where
     *      elevatorID is the ID of the given elevator
     *      currentFloor is the floor the elevator is currently at
     *      destinationFloor is the highest (if state is UP)
     *              or lowest (if state is DOWN) current pending job of a given elevator (null if state is IDLE)
     *      state is the current state of the elevator
     */
    StatusQuadruple[] status();
}
