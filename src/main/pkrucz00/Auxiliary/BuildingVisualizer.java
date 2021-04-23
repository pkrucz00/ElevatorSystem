package pkrucz00.Auxiliary;

import pkrucz00.mainClasses.Elevator;

import java.util.ArrayList;

public class BuildingVisualizer {
    private static final String EMPTY_CELL = "  ";
    private static final String FRAME_SEGMENT = "-";
    private static final String CELL_SEGMENT = "|";

    private Building building;

    public BuildingVisualizer(Building building){
        this.building = building;
    }

    // -------- main function ---------------
    public String draw(int x1, int x2, int y1, int y2){
        StringBuilder builder = new StringBuilder();

        builder.append(drawHeader(x1, x2));
        for (int i = y2 + 1; i >= y1 - 1; i--){
            builder.append(String.format("%3d: ", i));
            for (int j = x1; j <= x2 + 1; j++){
                if (i < y1 || i > y2){
                    builder.append(drawFrame(j <= x2 -1));
                } else {
                    builder.append(CELL_SEGMENT);
                    if (j < x2) {
                        builder.append(String.format("%2s", drawElevator(i, j)));
                    }
                }
            }
            if (y1 <= i && i <= y2)
                builder.append(String.format("  %2s", drawPeopleNumber(i)));
            else
                builder.append("  PPL");  //PPL - people)
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    // --------------helpers ---------------
    private String drawHeader(int x1, int x2) {
        StringBuilder builder = new StringBuilder();
        builder.append("     ");
        for (int j = x1; j < x2; j++) {
            builder.append(String.format("%3d", j));
        }

        builder.append(System.lineSeparator());
        return builder.toString();
    }

    private String drawFrame(boolean innerSegment) {
        if (innerSegment) {
            return FRAME_SEGMENT + FRAME_SEGMENT + FRAME_SEGMENT;
        } else {
            return FRAME_SEGMENT;
        }
    }

    private String drawElevator(int i, int elevatorID){
        Elevator[] elevators = this.building.getElevatorSystem().getElevators();
        Elevator elevator = elevators[elevatorID];
        if (elevator != null && elevator.getCurrentFloor() == i) {
            return elevator.toString();
        } else {
            return EMPTY_CELL;
        }
    }

    private String drawPeopleNumber(int i){
        ArrayList<Person> peopleOnTheFloor = this.building.getPeopleOnStoreys().get(i);
        int noPeople = peopleOnTheFloor.size();

        if (noPeople != 0) {
            return Integer.toString(noPeople);
        } else {
            return EMPTY_CELL;
        }
    }

}
