public class ElevatorB extends Elevator {
    public ElevatorB(DecisionMaking decisionMakingIn, int idIn) {
        super(decisionMakingIn, idIn);
        setMoveSpeed(400);
        setMax(6);
    }

    @Override
    public boolean touchable(int floor) {
        return floor % 2 == 1 && floor <= 20;
    }

    @Override
    public String getType() {
        return "B";
    }
}
