package unitTests;

import org.junit.Test;
import pkrucz00.Auxiliary.Building;
import pkrucz00.Auxiliary.Person;
import pkrucz00.mainClasses.ElevatorSystem;
import pkrucz00.mainClasses.Elevator;
import pkrucz00.mainClasses.ElevatorState;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BuildingUnitTest {
    int noElevators = 16;
    int noStoreys = 48;

    @Test
    public void buildingConstructionTest(){
        ElevatorSystem elevSys = new ElevatorSystem(noElevators, 0);
        Building testBuilding = new Building(noStoreys, elevSys);

        assertEquals(elevSys, testBuilding.getElevatorSystem());
        assertEquals(noStoreys, testBuilding.getMaxFloorNumber());
    }

    @Test
    public void buildingPickupTest(){
        ElevatorSystem elevSys = new ElevatorSystem(noElevators, 0);
        Building testBuilding = new Building(noStoreys, elevSys);

        Elevator givenElevator = testBuilding.sendRequestForElevator(2, true);
        elevSys.step();         // getting on first floor
        assertTrue(Arrays.stream(elevSys.status()).anyMatch((s) -> s.destFloor == 2));
        assertTrue(Arrays.stream(elevSys.status()).anyMatch((s) -> s.state == ElevatorState.UP));

        elevSys.step();         // getting on second floor
        elevSys.step();         // opening the doors
        elevSys.step();         // closing doors and going idle

        assertTrue(Arrays.stream(elevSys.status()).anyMatch((s) -> s.currFloor == 2));
        assertTrue(Arrays.stream(elevSys.status()).allMatch((s) -> s.state == ElevatorState.IDLE));
    }

    @Test
    public void addingAndDeletingPeopleTest(){
        ElevatorSystem elevSys = new ElevatorSystem(noElevators, 0);
        Building testBuilding = new Building(noStoreys, elevSys);

        Person testPerson1 = new Person(1, 0, 1, testBuilding);
        Person testPerson2 = new Person(2, 0, 1, testBuilding);

        ArrayList<ArrayList<Person>> people2dList = testBuilding.getPeopleOnStoreys();
        assertEquals(testPerson1, people2dList.get(0).get(0));
        assertEquals(testPerson2, people2dList.get(0).get(1));

        testPerson1.changeCurrentFloor(1);
        assertEquals(testPerson1, people2dList.get(1).get(0));
        assertEquals(testPerson2, people2dList.get(0).get(0));

        testBuilding.deletePerson(testPerson2);
        assertEquals(testPerson1, people2dList.get(1).get(0));
        assertEquals(new ArrayList<Person>(),
                testBuilding.getPeopleOnStoreys().get(0));  // no one should be on the ground floor
    }
}
