public class ElevatorC extends Elevator {
    public ElevatorC(DecisionMaking decisionMakingIn, int idIn) {
        super(decisionMakingIn, idIn);
        setMoveSpeed(200);
        setMax(4);
    }

    @Override
    public boolean touchable(int floor) {
        return floor >= 1 && floor <= 3 || floor >= 18 && floor <= 20;
    }

    @Override
    public String getType() {
        return "C";
    }
}
