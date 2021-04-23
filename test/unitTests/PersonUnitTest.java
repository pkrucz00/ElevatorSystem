package unitTests;

import org.junit.Test;
import pkrucz00.Auxiliary.Building;
import pkrucz00.Auxiliary.Person;
import pkrucz00.mainClasses.ElevatorSystem;

import java.util.ArrayList;
import static org.junit.Assert.*;

public class PersonUnitTest {
    int noElevators = 16;
    int noStoreys = 48;


    @Test
    public void personConstructionTest(){
        ElevatorSystem elevSys = new ElevatorSystem(noElevators, 0);
        Building building = new Building(noStoreys, elevSys);
        Person testPerson = new Person(0, 0, 1, building);

        assertEquals(0, testPerson.getPersonID());
        assertEquals(0, testPerson.getCurrentFloor());
        assertEquals(1, testPerson.getDestination());
        assertEquals(building, testPerson.getBuilding());
    }

    @Test
    public void addElevatorTest(){
        ElevatorSystem elevSys = new ElevatorSystem(noElevators, 0);
        Building building = new Building(noStoreys, elevSys);
        Person testPerson = new Person(0, 0, 1, building);

        assertNull(testPerson.getElevator());

        testPerson.sendRequestForElevator();
        assertNotNull(testPerson.getElevator());
    }

    @Test
    public void rideTheElevatorTest(){
        ElevatorSystem elevSys = new ElevatorSystem(noElevators, 0);
        Building building = new Building(noStoreys, elevSys);
        Person testPerson = new Person(0, 1, 2, building);

        testPerson.sendRequestForElevator();
        for (int i = 0; i < 2; i++){
            elevSys.step();
            testPerson.observeTheElevator();
        }
        assertTrue(testPerson.getElevator().isDoorOpened());
        assertTrue(testPerson.getElevator().getJobs().contains(2));  //we check if the person gave the elevator a request

        elevSys.step();  //we go one step up
        assertEquals(2, testPerson.getCurrentFloor());
        assertFalse(testPerson.getElevator().isDoorOpened());

        elevSys.step();  // doors open and the person is goes out
        assertEquals(2, testPerson.getCurrentFloor());
        assertEquals(new ArrayList<Person>(), building.getPeopleOnStoreys().get(2));  // the floor should be empty
    }

}
