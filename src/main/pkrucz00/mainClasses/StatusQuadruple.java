package pkrucz00.mainClasses;

import java.util.Objects;

import static java.lang.Math.abs;

public class StatusQuadruple {
    public final int elevatorID;  //everyone can access, no one can change
    public final int currFloor;
    public final Integer destFloor;
    public final ElevatorState state;

    public StatusQuadruple(int elevatorID, int currFloor, Integer destFloor, ElevatorState state){
        this.elevatorID = elevatorID;
        this.currFloor = currFloor;
        this.destFloor = destFloor;
        this.state = state;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusQuadruple that = (StatusQuadruple) o;
        return elevatorID == that.elevatorID &&
                currFloor == that.currFloor &&
                Objects.equals(destFloor, that.destFloor) &&
                state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(elevatorID, currFloor, destFloor, state);
    }
}
