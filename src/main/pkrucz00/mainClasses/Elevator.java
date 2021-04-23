package pkrucz00.mainClasses;

import pkrucz00.Auxiliary.Person;


import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.Iterator;

import static pkrucz00.mainClasses.ElevatorState.*;

public class Elevator {
    /**
     * Elevator acts more or less like a finite state machine.
     * It has three states: IDLE, UP and DOWN which can change after calling `step()`
     * The changes are dependant on the current floor and the `jobs` structure, which stores
     * all storeys the elevator needs to go to.
     *
     * The changes are stopped if the doors are open.
     * Opening of the door is indicated by the `doorOpened` flag and takes one step
     */

    private final int ElevatorID;
    private int currentFloor;
    private ElevatorState currentState;
    private boolean doorOpened;  //open door prevent the elevator from moving

    /**
     * All people inside the elevator are stored in this tree HashSet
     *
     * Currently the elevator has infinite capacity. It's set so it's easier to see the mechanism of the system.
     * It can be implemented in the future
     */
    private final Set<Person> peopleInside = new HashSet<>();

    /**  currentJobs needs to allow quick
     *      - inserting and removing elements by value
     *      - finding the successor and the predecessor
     *  NavigableSet seems perfect for this job (All above operations in  max O(logn) time)
     */
    private final NavigableSet<Integer> jobs = new TreeSet<>();

    public Elevator(int elevatorID, int initialFloor){
        this.ElevatorID = elevatorID;

        this.currentFloor = initialFloor;
        this.currentState = ElevatorState.IDLE;
        this.doorOpened = false;
    }

    /**
     * Step is a singular (quantized) "turn" the elevator can make
     * In one step the elevator can EITHER:
     *      - open the door, leave people and delete current job OR
     *      - close the door, change state and move
     */

    public void step(){
        if (this.jobs.contains(this.currentFloor)){  //we arrived at our destination
            this.openDoor();
            this.leavePeople();
            this.deleteJob(this.currentFloor);
        } else {
            this.closeDoor();
            this.changeState();
            this.move();
        }
    }

    private void move(){
        if (this.doorOpened){
            throw new AssertionError("Error! Moving elevator with opened doors!");
        }
        switch (this.currentState) {
            case UP -> this.currentFloor += 1;
            case DOWN -> this.currentFloor -= 1;
        }
        for (Person person: peopleInside){
            person.changeCurrentFloor(this.currentFloor);
        }
    }

    /**
     *  `changeState` is the main function behind the "logic" of the elevator.
     *  The elevator changes to:
     *      - IDLE if there are no jobs to do an of right now
     *      - UP if:
     *          -- (for currentState == IDLE or UP) current floor is lower than the destination
     *          -- (for currentState == DOWN) current floor is lower and there is no job lower than current floor
     *      - DOWN if:
     *          -- (for currentState == IDLE or DOWN) current floor is higher than the destination
     *          -- (for currentState == UP) current floor is higher and there is no job higher than current floor
     *
     *   Logic defined this way means that elevator that was going UP will "prefer" to go UP if possible
     *   (the same goes for DOWN). Further explanation is wrote down in README.md
     */

    private void changeState() {
        if (jobs.contains(this.currentFloor)){
            throw new AssertionError("Changing state of elevator no " + ElevatorID + " on destination floor");
        }

        // empty job set -> going to IDLE state
        if (jobs.size() == 0) {
            this.currentState = IDLE;
            return;
        }

        Integer closestFloorDownwards = jobs.floor(this.currentFloor);
        Integer closestFloorUpwards = jobs.ceiling(this.currentFloor);

        switch (this.currentState) {
            // if elevator is in idle state then we need to go to the closest job possible
            case IDLE -> {
                Integer closestFloor =
                        findClosestFloor(this.currentFloor, closestFloorUpwards, closestFloorDownwards);

                if (closestFloor < this.currentFloor) {
                    this.currentState = DOWN;
                } else {
                    this.currentState = UP;
                }
            }
            case DOWN -> {
                if (closestFloorDownwards != null) {    // if there exists a predecessor in `jobs`
                    this.currentState = DOWN;          // let's continue going down
                } else {
                    this.currentState = UP;            // otherwise we need to go up
                }
            }
            case UP -> {
                if (closestFloorUpwards != null) {      // if there exists a successor in `jobs`
                    this.currentState = UP;             // let's continue going up
                } else {
                    this.currentState = DOWN;           // otherwise we need to go down
                }
            }
        }
    }


    private Integer findClosestFloor(int floorNumber, Integer higherFloor, Integer lowerFloor){
        if (higherFloor == null) {return lowerFloor;}
        if (lowerFloor == null) {return higherFloor;}

        return higherFloor - floorNumber < floorNumber - lowerFloor ? higherFloor : lowerFloor;

    }

    // ------------- maintaining people in elevator ---------------------------
    public void addPerson(Person person){
        if (person.getCurrentFloor() != this.currentFloor){
            throw new IllegalArgumentException("Person with ID " + person.getPersonID() +
                    " enters elevator with ID " + this.ElevatorID + " on wrong floor");
        }
        if (!doorOpened) {
           throw new IllegalArgumentException("Person with ID " + person.getPersonID() +
                   " wants to go to closed elevator with ID " + this.ElevatorID);
        } else if (peopleInside.contains(person)) {
            throw new IllegalArgumentException("Person with ID " + person.getPersonID() +
                    " is already in elevator with ID " + this.ElevatorID);
        }
        peopleInside.add(person);

    }

    public void leavePeople(){
        Iterator<Person> i = this.peopleInside.iterator();

        while (i.hasNext()){
            Person person = i.next();
            if (person.getDestination() == currentFloor){
                person.goOutTheElevator();
                i.remove();
            }
        }
    }

    // ------------jobs functions---------------
    public void addJob(int floorNumber){
        if (!doorOpened || floorNumber != this.currentFloor)
            this.jobs.add(floorNumber);
    }

    //only by finishing a job it can be deleted from `jobs`
    private void deleteJob(int floorNumber){
        jobs.remove(floorNumber);
    }


    //------------door functions--------
    public void openDoor(){
        doorOpened = true;
    }

    public void closeDoor(){
        doorOpened = false;
    }


    //--------------getters------------
    public int getElevatorID() {
        return ElevatorID;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Integer getDestinationFloor() {
        // it's possible to empty jobs before changing the state;
        // in this case our destination is the current floor
        return switch (this.currentState){
            case UP -> this.jobs.isEmpty() ? this.currentFloor : this.jobs.last();
            case DOWN -> this.jobs.isEmpty() ? this.currentFloor : this.jobs.first();
            case IDLE -> null;
        };
    }

    public ElevatorState getCurrentState() {
        return currentState;
    }

    public NavigableSet<Integer> getJobs() {
        return jobs;
    }

    public boolean isDoorOpened() {
        return doorOpened;
    }

    public Set<Person> getPeopleInside() {
        return peopleInside;
    }

    public int getNoPeopleInside(){
        return this.peopleInside.size();
    }

    @Override
    public String toString() {
        return this.doorOpened ? "*" : Integer.toString(this.getNoPeopleInside());
    }
}
