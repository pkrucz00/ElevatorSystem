package pkrucz00.Simulation;

public enum ScenarioType {
    UNIFORM,  // uniform distribution of people in equal time intervals
    BURSTS,     // 2 to 6 people go to the elevator in equal time intervals
    IRREGULARLY;  // one person spawns on random floor in irregular time intervals

    public static ScenarioType parseString(String scenarioName){
        return switch (scenarioName.toLowerCase()){
            case "uniform" -> UNIFORM;
            case "bursts" -> BURSTS;
            case "irregularly" -> IRREGULARLY;
            default -> throw new IllegalArgumentException(scenarioName + " is not a valid scenario name");
        };
    }
}
