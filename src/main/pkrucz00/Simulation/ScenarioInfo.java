package pkrucz00.Simulation;

public class ScenarioInfo {
    public final int noElevators;
    public final int noStoreys;
    public final int noIterations;
    public final int sleep;
    public final int reversedIntensity;
    public final ScenarioType scenario;

    public ScenarioInfo(String[] args){
        try {
            this.noElevators = Integer.parseInt(args[0]);
            this.noStoreys = Integer.parseInt(args[1]);
            this.noIterations = Integer.parseInt(args[2]);
            this.reversedIntensity = Integer.parseInt(args[3]);
            this.sleep = Integer.parseInt(args[4]);
            this.scenario = ScenarioType.parseString(args[5]);
        } catch (IllegalArgumentException ex){
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}
