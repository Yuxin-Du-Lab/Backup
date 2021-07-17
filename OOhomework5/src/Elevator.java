import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.List;

public class Elevator extends Thread {
    private int currentFloor;
    private boolean doorOpened;
    private List<PersonRequest> container;
    private final Dispatch dispatch;
    private final RequestTable requestTable;

    public Elevator(RequestTable requestTableIn, Dispatch dispatchIn) {
        currentFloor = 1;
        doorOpened = false;
        container = new ArrayList<>();
        this.requestTable = requestTableIn;
        dispatch = dispatchIn;
        dispatch.init(this);
    }

    public RequestTable getRequestTable() {
        return requestTable;
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            String order;
            synchronized (requestTable) {
                //System.out.println("Dispatch:" + dispatch.state);
                order = dispatch.decide();
                //System.out.println(">>>" + order);
            }
            //System.out.println("I get order>>> " + order + " at " + currentFloor);
            switch (order) {
                case "OPEN":
                    openDoor();
                    unLoad();
                    synchronized (requestTable) {
                        load(requestTable.getFloorList(currentFloor));
                    }
                    runSleep(400);
                    closeDoor();
                    break;
                case "UP":
                    upGoing();
                    useOutput("ARRIVE-" + currentFloor);
                    break;
                case "DOWN":
                    downGoing();
                    useOutput("ARRIVE-" + currentFloor);
                    break;
                case "WAIT":
                    // take care here, multi-elevators will exception!
                    if (getRequestTable().getIsFinished()
                            & getRequestTable().getWaitingNum() == 0) {
                        return;
                    }
                    requestTable.runWait();
                    break;
                case "OPENING":
                    if (!doorOpened) {
                        openDoor();
                        runSleep(400);
                    }
                    synchronized (requestTable) {
                        load(requestTable.getFloorList(currentFloor));
                    }
                    if (!isFull() & !requestTable.getIsFinished()) {
                        ((DispatchMorning) dispatch).standby();
                    }
                    break;
                case "CLOSE":
                    if (doorOpened) {
                        closeDoor();
                    }
                    break;
                default:
                    //error order
                    System.out.println("error dispatch order returned");
                    System.out.println("order: " + order);
                    break;
            }
        }
    }

    private void upGoing() {
        if (currentFloor < 20) {
            currentFloor++;
            runSleep(400);
        } else {
            System.out.println("error, out of boundary upside");
        }
    }

    private void downGoing() {
        if (currentFloor > 1) {
            currentFloor--;
            runSleep(400);
        } else {
            System.out.println("error, out of boundary downside");
        }
    }

    private void openDoor() {
        if (!doorOpened) {
            useOutput("OPEN-" + currentFloor);
            doorOpened = true;
        } else {
            System.out.println("error, door has been opened");
        }
    }

    private void closeDoor() {
        if (doorOpened) {
            useOutput("CLOSE-" + currentFloor);
            doorOpened = false;
        } else {
            System.out.println("error, door has been closed");
        }
    }

    private void load(List<PersonRequest> currentList) {
        if (!doorOpened) {
            System.out.println("Door Closed!");
            return;
        }
        if (dispatch.getState() == 0 & container.size() == 0) {
            // init load
            initLoad(currentList);
        }
        for (int index = 0; index < currentList.size(); index++) {
            if (isFull()) {
                break;
            }
            if (currentList.get(index).getToFloor() > currentFloor && dispatch.getState() == 1) {
                //
                useOutput("IN-" + currentList.get(index).getPersonId() + "-" + currentFloor);
                container.add(currentList.get(index));
                currentList.remove(index);
                index--;
            } else if (currentList.get(index).getToFloor() < currentFloor
                    & dispatch.getState() == -1) {
                useOutput("IN-" + currentList.get(index).getPersonId() + "-" + currentFloor);
                container.add(currentList.get(index));
                currentList.remove(index);
                index--;
            }
        }
    }

    // state == 0
    private void initLoad(List<PersonRequest> currentList) {
        if (currentList.size() > 0) {
            useOutput("IN-" + currentList.get(0).getPersonId() + "-" + currentFloor);
            container.add(currentList.get(0));
            currentList.remove(0);
            dispatch.initInside();
        }
    }

    private void unLoad() {
        if (!doorOpened) {
            System.out.println("Door is Closed!");
            return;
        }
        if (container.size() > 0) {
            for (int index = 0; index < container.size(); index++) {
                if (container.get(index).getToFloor() == currentFloor) {
                    useOutput("OUT-" + container.get(index).getPersonId() + "-" + currentFloor);
                    container.remove(index);
                    index--;
                }
            }
        }
    }

    public boolean isFull() {
        int maximum = 6;
        return container.size() == maximum;
    }

    private void useOutput(String line) {
        try {
            Output.print(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runSleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isDoorOpened() {
        return doorOpened;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public List<PersonRequest> getContainer() {
        return container;
    }
}
