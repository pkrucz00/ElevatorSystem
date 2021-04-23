package unitTests;

import org.junit.Test;
import pkrucz00.mainClasses.ElevatorSystem;
import pkrucz00.mainClasses.Elevator;
import pkrucz00.mainClasses.ElevatorState;
import pkrucz00.mainClasses.StatusQuadruple;

import static org.junit.Assert.*;


public class ElevatorSystemTest {
    int noElevators = 16;

    @Test
    public void ElevatorSystemCreationTest(){
        ElevatorSystem testElevSys = new ElevatorSystem(noElevators, 0);
        Elevator[] elevators = testElevSys.getElevators();

        for (int i = 0; i < noElevators; i++) {
            assertEquals(i, elevators[i].getElevatorID());
            assertEquals(0, elevators[i].getCurrentFloor());
            assertEquals(ElevatorState.IDLE, elevators[i].getCurrentState());
        }
    }

    @Test
    public void ElevatorSystemStatusMethodTest(){
        ElevatorSystem testElevSys = new ElevatorSystem(noElevators, 0);
        Elevator[] elevators = testElevSys.getElevators();

        StatusQuadruple[] expectedOutput = new StatusQuadruple[noElevators];
        for (int i = 0; i < noElevators; i++){
            expectedOutput[i] = new StatusQuadruple(elevators[i].getElevatorID(),
                    elevators[i].getCurrentFloor(),
                    elevators[i].getDestinationFloor(),
                    elevators[i].getCurrentState());
        }
        assertArrayEquals(expectedOutput, testElevSys.status());
    }

    @Test
    public void ElevatorSystemSimpleUpdateTest(){
        int noElevators = 1;

        ElevatorSystem testElevSys = new ElevatorSystem(noElevators, 0);

        testElevSys.pickup(0, true);
        testElevSys.pickup(16, false);
        testElevSys.pickup(8, true);

        Elevator onlyElevator = testElevSys.getElevators()[0];
        assertTrue(onlyElevator.getJobs().contains(0));
        assertTrue(onlyElevator.getJobs().contains(16));
        assertTrue(onlyElevator.getJobs().contains(8));
    }

    @Test
    public void ElevatorSystemSimpleStepTest(){
        int noElevators = 1;

        ElevatorSystem testElevSys = new ElevatorSystem(noElevators, 0);

        testElevSys.pickup(1, true);  //sending signal to the elevator
        testElevSys.step();     // elevator goes one floor up
        testElevSys.step();     // elevator arrives and opens the door

        Elevator onlyElevator = testElevSys.getElevators()[0];
        assertEquals(1, onlyElevator.getCurrentFloor());
    }

    @Test
    public void ElevatorSystemUsageTest(){

    }


    private StatusQuadruple[] getExpectedStatus(Elevator[] elevators, int n){
        StatusQuadruple[] expectedOutput = new StatusQuadruple[n];
        for (int i = 0; i < n; i++){
            expectedOutput[i] = new StatusQuadruple(elevators[i].getElevatorID(),
                    elevators[i].getCurrentFloor(),
                    elevators[i].getDestinationFloor(),
                    elevators[i].getCurrentState());
        }
        return expectedOutput;
    }
}
