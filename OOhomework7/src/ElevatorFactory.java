public class ElevatorFactory {
    public Elevator build(String mode, int idIn, String type) {
        if (mode.equals("Random") || mode.equals("Night")) {
            DecisionMaking decisionMaking = new DecisionMaking();
            switch (type) {
                case "A":
                    return new Elevator(decisionMaking, idIn);
                case "B":
                    return new ElevatorB(decisionMaking, idIn);
                case "C":
                    return new ElevatorC(decisionMaking, idIn);
                default:
                    return null;
            }
        } else if (mode.equals("Morning")) {
            DecisionMaking decisionMaking = new DecisionMakingMorning();
            switch (type) {
                case "A":
                    return new Elevator(decisionMaking, idIn);
                case "B":
                    return new ElevatorB(decisionMaking, idIn);
                case "C":
                    return new ElevatorC(decisionMaking, idIn);
                default:
                    return null;
            }
        }
        return null;
    }
}
