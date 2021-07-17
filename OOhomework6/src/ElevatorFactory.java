public class ElevatorFactory {
    public Elevator build(String mode, int idIn) {
        if (mode.equals("Random") || mode.equals("Night")) {
            DecisionMaking decisionMaking = new DecisionMaking();
            return new Elevator(decisionMaking, idIn);
        } else if (mode.equals("Morning")) {
            DecisionMaking decisionMaking = new DecisionMakingMorning();
            return new Elevator(decisionMaking, idIn);
        }
        return null;
    }
}
