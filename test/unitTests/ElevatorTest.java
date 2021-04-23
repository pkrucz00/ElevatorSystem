package unitTests;

import org.junit.Test;
import pkrucz00.Auxiliary.Person;
import pkrucz00.mainClasses.Elevator;
import pkrucz00.mainClasses.ElevatorState;

import java.util.HashSet;

import static org.junit.Assert.*;

public class ElevatorTest {

    @Test
    public void ElevatorCreationTest(){
        Elevator testElev = new Elevator(0,0);
        assertEquals(0, testElev.getElevatorID());
        assertEquals(0, testElev.getCurrentFloor());
        assertEquals(ElevatorState.IDLE, testElev.getCurrentState());
    }

    @Test
    public void ElevatorMoveTest(){
        Elevator testElev = new Elevator(0,0);

        testElev.addJob(2);
        testElev.step();  //go one floor up
        assertEquals(ElevatorState.UP, testElev.getCurrentState());
        assertEquals(1, testElev.getCurrentFloor());

        testElev.step();    // go one flor up
        assertEquals(2, testElev.getCurrentFloor());

        testElev.step();   //opening the door
        assertTrue(testElev.isDoorOpened());

        testElev.step();    //close the door and change state (to IDLE)
        assertFalse(testElev.isDoorOpened());
        assertEquals(ElevatorState.IDLE, testElev.getCurrentState());

        for (int i = 0; i < 100; i++) {
            testElev.step(); //pretty much do nothing
        }
        assertFalse(testElev.isDoorOpened());
        assertEquals(ElevatorState.IDLE, testElev.getCurrentState());
    }

    @Test
    public void personEnteringTheElevatorTest(){
        /*
          Simulating entering the elevator on the floor on which the elevator is currently IDLE
          No person object here, we want to test only the moves of the elevator
         */
        Elevator testElev = new Elevator(0,0);

        testElev.addJob(0);  //the person clicks the button
        testElev.step();  //the doors should open
        assertTrue(testElev.isDoorOpened());

        testElev.addJob(1);  //the person clicks the button
        testElev.step();   //the doors close...
        assertFalse(testElev.isDoorOpened());
        assertEquals(ElevatorState.UP, testElev.getCurrentState()); //.. elevator changes state to UP ...
        assertEquals(1, testElev.getCurrentFloor());        // ... and goes up


        testElev.step();  //open the doors, the person can safely exit
        assertTrue(testElev.isDoorOpened());

        testElev.step();  //close the doors
        assertFalse(testElev.isDoorOpened());
        assertEquals(ElevatorState.IDLE, testElev.getCurrentState());

    }

    @Test
    public void openingAndClosingDoorsTest(){
        Elevator testElev = new Elevator(0,0);

        Person person1 = new Person(0, 0, 1, null);
        Person person2 = new Person(1, 1, 0, null);

        assertEquals(new HashSet<Person>(), testElev.getPeopleInside());
        testElev.addJob(person1.getCurrentFloor());

        testElev.step();
        testElev.addPerson(person1);
        assertTrue(testElev.getPeopleInside().contains(person1));

        testElev.addJob(person1.getDestination());

        testElev.step();
        testElev.addJob(person2.getCurrentFloor());

        testElev.step();
        assertFalse(testElev.getPeopleInside().contains(person1));  //person1 should've already left
        testElev.addPerson(person2);
        assertTrue(testElev.getPeopleInside().contains(person2));
    }

}
