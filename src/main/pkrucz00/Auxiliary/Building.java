package pkrucz00.Auxiliary;

import pkrucz00.mainClasses.Elevator;
import pkrucz00.mainClasses.ElevatorSystem;

import java.util.ArrayList;

public class Building {
    private final int maxFloorNumber;
    private final ElevatorSystem elevatorSystem;
    private final ArrayList<ArrayList<Person>> peopleOnStoreys;

    public Building(int maxFloorNumber, ElevatorSystem elevatorSystem){
        this.maxFloorNumber = maxFloorNumber;
        this.elevatorSystem = elevatorSystem;

        this.peopleOnStoreys = new ArrayList<>(maxFloorNumber + 1);  // size of array = noFloors + ground floor
        for (int i = 0; i <= maxFloorNumber; i++){
            peopleOnStoreys.add(new ArrayList<>());
        }
    }

    // -----------maintaining people in building ------------------
    public void addPerson(Person person){
        int floorNumber = person.getCurrentFloor();
        if (0 <= floorNumber && floorNumber <= maxFloorNumber) {
            this.peopleOnStoreys.get(floorNumber).add(person);
        } else {
            throw new IllegalArgumentException("Error! Adding person on floor number" + floorNumber + ". " +
                    "(Possible range: [0, " + maxFloorNumber + "]");
        }
    }

    public void deletePerson(Person person){
        int floorNumber = person.getCurrentFloor();
        if (0 <= floorNumber && floorNumber <= maxFloorNumber) {
            this.peopleOnStoreys.get(floorNumber).remove(person);
        } else {
            throw new IllegalArgumentException("Exception! Adding person on floor number" + floorNumber + ". " +
                    "(Possible range: [0, " + maxFloorNumber + "]");
        }
    }

    public void makePeopleObserveTheElevator(){
        ArrayList<Person> allPeopleInBuilding = flattedArrayList(this.peopleOnStoreys);
        allPeopleInBuilding.forEach(Person::observeTheElevator);
    }


    private ArrayList<Person> flattedArrayList(ArrayList<ArrayList<Person>> nestedArrayList){
        ArrayList<Person> flattenedArrayList = new ArrayList<>();
        nestedArrayList.forEach(flattenedArrayList::addAll);
        return flattenedArrayList;
    }


    public Elevator sendRequestForElevator(int currentFloor, boolean isGoingUp){
        return elevatorSystem.pickup(currentFloor, isGoingUp);
    }


    //--------------------- getters ----------------
    public ElevatorSystem getElevatorSystem() {
        return elevatorSystem;
    }

    public int getMaxFloorNumber(){
        return maxFloorNumber;
    }

    public ArrayList<ArrayList<Person>> getPeopleOnStoreys() {
        return peopleOnStoreys;
    }

    public int getNoPeopleInBuilding(){
        return flattedArrayList(this.peopleOnStoreys).size();
    }


    //--------shows the whole building with its current state
    public String toString(){
        BuildingVisualizer visualizer = new BuildingVisualizer(this);
        int rightBound = this.elevatorSystem.getElevators().length;

        return visualizer.draw(0, rightBound, 0, this.maxFloorNumber);
    }
}
