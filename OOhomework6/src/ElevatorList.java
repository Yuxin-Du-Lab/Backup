import java.util.ArrayList;
import java.util.List;

public class ElevatorList {
    private List<Elevator> elevatorList = new ArrayList<>();

    public synchronized void addElevator(Elevator elevatorIn) {
        elevatorList.add(elevatorIn);
    }

    public synchronized List<Elevator> getElevatorList() {
        return elevatorList;
    }

    public synchronized void setInputEnd() {
        for (Elevator elevator : elevatorList) {
            elevator.setInputEnd();
        }
    }
}
