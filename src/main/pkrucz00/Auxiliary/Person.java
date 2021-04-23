package pkrucz00.Auxiliary;

import pkrucz00.mainClasses.Elevator;

import java.util.Objects;

public class Person {
    private final int personID;
    private final int destination;
    private int currentFloor;
    private final Building building;

    private Elevator elevator;

    public Person(int personID, int initialFloor, int destination, Building building) {
        this.personID = personID;
        this.currentFloor = initialFloor;
        this.destination = destination;
        this.building = building;
        this.elevator = null;

        if (currentFloor == destination){
            throw new IllegalArgumentException("Person is on the same floor it wants to go to");
        }
        if (building != null) {
            this.building.addPerson(this);
        }
    }

    // ----------- functions for using the elevator --------------------
    public void sendRequestForElevator(){  // clicking the button to call the elevator
        boolean isGoingUp = destination > currentFloor;
        Elevator elevator = this.building.sendRequestForElevator(currentFloor, isGoingUp);
        this.setElevator(elevator);
    }

    public void observeTheElevator(){  //observing the elevator and getting into it when it's ready
        if (this.shouldGoToElevator()) {
            elevator.addJob(this.destination);
            elevator.addPerson(this);
        }
    }

    private boolean shouldGoToElevator(){                       //we need to make sure that
        return elevator != null &&                                   // the elevator was set
                this.currentFloor == elevator.getCurrentFloor() &&   //we are on the same floor
                elevator.isDoorOpened() &&                           // the doors have opened
                !this.elevator.getPeopleInside().contains(this);     // we haven't already got in the elevator
    }

    public void changeCurrentFloor(int newFloor){
        if (building != null)   this.building.deletePerson(this);
        this.currentFloor = newFloor;
        if (building != null)   this.building.addPerson(this);
    }


    public void goOutTheElevator(){
        if (building != null)
            this.building.deletePerson(this);   //we don't need to know what happens after the person left the elevator
                                                // so we delete this person
    }

    //----------setters---------------
    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }

    // ----------getters ------------
    public int getPersonID() {
        return personID;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getDestination() {
        return destination;
    }

    public Elevator getElevator() {
        return elevator;
    }

    public Building getBuilding() {
        return building;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return personID == person.personID &&
                destination == person.destination;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personID, destination);
    }
}

