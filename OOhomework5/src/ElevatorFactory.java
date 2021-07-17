public class ElevatorFactory {
    public Elevator build(RequestTable requestTable,String mode) {
        if (mode.equals("Random") || mode.equals("Night")) {
            Dispatch dispatch = new Dispatch();
            return new Elevator(requestTable, dispatch);
        } else if (mode.equals("Morning")) {
            Dispatch dispatch = new DispatchMorning();
            return new Elevator(requestTable, dispatch);
        }
        return null;
    }
}
