import com.oocourse.elevator3.PersonRequest;

import java.util.List;

public class Dispatch extends Thread {
    private final WaitQueue waitQueue;
    private final ElevatorList elevatorList;
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
            request = null;
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
                    if (dispatchCore(elevatorList.getListC())) {
                        continue;
                    }
                    if (dispatchCore(elevatorList.getListB())) {
                        continue;
                    }
                    if (dispatchCore(elevatorList.getListA())) {
                        continue;
                    }
                    System.out.println(">>>FATAL ERROR!\ncannot dispatch request:" + request);
                }
            }
        }
        synchronized (elevatorList) {
            elevatorList.notifyall();
        }
    }

    private boolean dispatchCore(List<Elevator> list) {
        boolean dispatched = false;
        for (Elevator check : list) {
            // if cannot direct touch, return
            if (!directAble(request, check)) {
                return false;
            }
            if (pattern == 1) {
                // Morning patter: Load at least
                break;
            }
            if (check.getLocalTable().getWaitingNum() + check.getInsideNum() >= check.getMax()) {
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
            // Load at least
            list.get(findMin(list))
                    .getLocalTable().receiveRequest(request);
            dispatched = true;
        }
        return dispatched;
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

    private int findMin(List<Elevator> list) {
        int min = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getLocalTable().getWaitingNum()
                    < list.get(min).getLocalTable().getWaitingNum()) {
                min = i;
            }
        }
        return min;
    }

    private boolean directAble(PersonRequest request, Elevator elevator) {
        return elevator.touchable(request.getFromFloor())
                && elevator.touchable(request.getToFloor());
    }
}
