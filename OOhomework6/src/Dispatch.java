import com.oocourse.elevator2.PersonRequest;

import java.util.List;

public class Dispatch extends Thread {
    private WaitQueue waitQueue = null;
    private ElevatorList elevatorList = null;
    private PersonRequest request = null;
    private final int pattern;

    public Dispatch(WaitQueue waitQueueIn, ElevatorList elevatorListIn, String patternIn) {
        waitQueue = waitQueueIn;
        elevatorList = elevatorListIn;
        if (patternIn.equals("Morning")) {
            pattern = 1;
        } else {
            pattern = 0;
        }
    }

    @Override
    public void run() {
        while (true) {
            synchronized (waitQueue) {
                if (waitQueue.getFinished() & waitQueue.getLenth() == 0) {
                    // input finished & wait queue process end
                    elevatorList.setInputEnd();
                    //System.out.println("Dispatch die!");
                    break;
                } else if (waitQueue.getLenth() == 0) {
                    // input not finished but wait queue proccess end
                    waitQueue.runWait();
                } else {
                    request = waitQueue.popRequest();
                }
            }
            if (request != null) {
                // get request from wait queue
                synchronized (elevatorList) {
                    boolean dispatched = false;
                    for (Elevator check : elevatorList.getElevatorList()) {
                        if (pattern == 1) {
                            break;
                        }
                        if (check.getLocalTable().getWaitingNum() >= 6) {
                            continue;
                        }
                        if (isLS(request, check)) {
                            // this request will be dispatched to this elevator local table
                            check.getLocalTable().receiveRequest(request);
                            dispatched = true;
                            break;
                        }
                    }
                    if (!dispatched) {
                        // no elevator LS
                        elevatorList.getElevatorList().get(findMin(elevatorList))
                                .getLocalTable().receiveRequest(request);
                    }
                    request = null;
                }
            }
        }
        synchronized (elevatorList) {
            for (Elevator elevator : elevatorList.getElevatorList()) {
                LocalTable table = elevator.getLocalTable();
                synchronized (table) {
                    table.notify();
                }
            }
        }
    }

    private boolean isLS(PersonRequest request, Elevator elevator) {
        int currentFloor = elevator.getCurrentFloor();
        int from = request.getFromFloor();
        int to = request.getToFloor();
        int state = elevator.getDecideMakingState();
        if (state == 0) {
            // this elevator is not moving
            return true;
        } else if (state > 0) {
            // this elevator is up going
            // request up going & from is in the way of elevator
            return from < to & from >= currentFloor;
        } else {
            // this elevator is down going
            // request down going & from is in the way of elevator
            return from > to & from <= currentFloor;
        }
    }

    private int findMin(ElevatorList elevatorList) {
        int min = 0;
        List<Elevator> list = elevatorList.getElevatorList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getLocalTable().getWaitingNum()
                    < list.get(min).getLocalTable().getWaitingNum()) {
                min = i;
            }
        }
        return min;
    }
}
