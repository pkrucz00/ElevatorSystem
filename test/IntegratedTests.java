import org.junit.Test;
import pkrucz00.Auxiliary.Building;
import pkrucz00.Auxiliary.Person;
import pkrucz00.mainClasses.ElevatorSystem;
import pkrucz00.mainClasses.Elevator;
import pkrucz00.mainClasses.ElevatorState;

import static org.junit.Assert.*;

public class IntegratedTests {
    @Test
    public void simpleTest(){
        int noElevators = 1;
        int noStoreys = 2;

        ElevatorSystem elevSys = new ElevatorSystem(noElevators, 0);
        Building testBuilding = new Building(noStoreys, elevSys);
        Elevator onlyElevator = elevSys.getElevators()[0];

        Person person1 = new Person(1, 1, 2, testBuilding);
        Person person2 = new Person(2, 2, 0, testBuilding);

        person1.sendRequestForElevator();

        elevSys.step();  //elevator goes up
        assertEquals(ElevatorState.UP, onlyElevator.getCurrentState());
        assertEquals(1, onlyElevator.getCurrentFloor());

        elevSys.step();  //opens the door
        person1.observeTheElevator();
        person2.sendRequestForElevator();

        elevSys.step();  //closes the door and goes up
        assertEquals(2, person1.getCurrentFloor());

        elevSys.step();  // opens the door
        assertEquals(0, onlyElevator.getNoPeopleInside());

        person2.observeTheElevator();
        assertEquals(1, onlyElevator.getNoPeopleInside());

        elevSys.step();  // closes the door
        assertEquals(ElevatorState.DOWN, onlyElevator.getCurrentState());

        elevSys.step();
        assertEquals(0, onlyElevator.getCurrentFloor());
        assertEquals(0, person2.getCurrentFloor());

        elevSys.step();  //letting person 2 go
        assertEquals(0, onlyElevator.getNoPeopleInside());
    }

}
