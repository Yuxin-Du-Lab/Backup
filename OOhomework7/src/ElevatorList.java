import java.util.ArrayList;
import java.util.List;

public class ElevatorList {
    private final List<Elevator> listA = new ArrayList<>();
    private final List<Elevator> listB = new ArrayList<>();
    private final List<Elevator> listC = new ArrayList<>();

    public synchronized void addElevator(Elevator elevatorIn) {
        //elevatorList.add(elevatorIn);
        switch (elevatorIn.getType()) {
            case "A":
                //System.out.println("it is a A elevator");
                listA.add(elevatorIn);
                break;
            case "B":
                //System.out.println("it is a B elevator");
                listB.add(elevatorIn);
                break;
            case "C":
                //System.out.println("it is a C elevator");
                listC.add(elevatorIn);
                break;
            default:
                break;
        }
    }

    public synchronized List<Elevator> getListA() {
        return listA;
    }

    public synchronized List<Elevator> getListB() {
        return listB;
    }

    public synchronized List<Elevator> getListC() {
        return listB;
    }

    public synchronized void setInputEnd() {
        for (Elevator elevator : listA) {
            elevator.setInputEnd();
        }
        for (Elevator elevator : listB) {
            elevator.setInputEnd();
        }
        for (Elevator elevator : listC) {
            elevator.setInputEnd();
        }
    }

    public synchronized void notifyall() {
        LocalTable table;
        for (Elevator elevator : listA) {
            table = elevator.getLocalTable();
            synchronized (table) {
                table.notify();
            }
        }
        for (Elevator elevator : listB) {
            table = elevator.getLocalTable();
            synchronized (table) {
                table.notify();
            }
        }
        for (Elevator elevator : listC) {
            table = elevator.getLocalTable();
            synchronized (table) {
                table.notify();
            }
        }
    }
}
