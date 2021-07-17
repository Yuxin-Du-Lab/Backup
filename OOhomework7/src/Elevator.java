import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.List;

public class Elevator extends Thread {
    private int currentFloor;
    private boolean doorOpened;
    private List<PersonRequest> container;
    private final DecisionMaking decisionMaking;
    private final LocalTable localTable;
    private final int id;
    private boolean inputEnd = false;
    private int moveSpeed;
    private int max;

    public Elevator(DecisionMaking decisionMakingIn, int idIn) {
        currentFloor = 1;
        doorOpened = false;
        container = new ArrayList<>();
        localTable = new LocalTable();
        decisionMaking = decisionMakingIn;
        decisionMaking.init(this);
        id = idIn;
        setMoveSpeed(600);
        setMax(8);
    }

    public LocalTable getLocalTable() {
        return localTable;
    }

    @Override
    public void run() {
        while (true) {
            String order;
            synchronized (localTable) {
                //System.out.println("Dispatch:" + dispatch.state);
                order = decisionMaking.decide();
                //System.out.println(">>>" + order);
            }
            //System.out.println("I get order>>> " + order + " at " + currentFloor);
            switch (order) {
                case "OPEN":
                    openDoor();
                    unLoad();
                    synchronized (localTable) {
                        load(localTable.getFloorList(currentFloor));
                    }
                    runSleep(400);
                    closeDoor();
                    break;
                case "UP":
                    upGoing();
                    useOutput("ARRIVE-" + currentFloor + "-" + id);
                    break;
                case "DOWN":
                    downGoing();
                    useOutput("ARRIVE-" + currentFloor + "-" + id);
                    break;
                case "WAIT":
                    // take care here, multi-elevators will exception!
                    if (inputEnd & getLocalTable().getWaitingNum() == 0) {
                        //System.out.println("Elevator-" + id + ": die!");
                        return;
                    }
                    localTable.runWait();
                    break;
                case "OPENING":
                    if (!doorOpened) {
                        openDoor();
                        runSleep(400);
                    }
                    synchronized (localTable) {
                        load(localTable.getFloorList(currentFloor));
                    }
                    if (!isFull() & !inputEnd) {
                        ((DecisionMakingMorning) decisionMaking).standby();
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
            runSleep(moveSpeed);
        } else {
            System.out.println("error, out of boundary upside");
        }
    }

    private void downGoing() {
        if (currentFloor > 1) {
            currentFloor--;
            runSleep(moveSpeed);
        } else {
            System.out.println("error, out of boundary downside");
        }
    }

    private void openDoor() {
        if (!touchable(currentFloor)) {
            System.out.println(">>>FATAL ERROR!\nYou cannot open door this floor!!");
            return;
        }
        if (!doorOpened) {
            useOutput("OPEN-" + currentFloor + "-" + id);
            doorOpened = true;
        } else {
            System.out.println("error, door has been opened");
        }
    }

    private void closeDoor() {
        if (doorOpened) {
            useOutput("CLOSE-" + currentFloor + "-" + id);
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
        if (decisionMaking.getState() == 0 & container.size() == 0) {
            // init load
            initLoad(currentList);
        }
        for (int index = 0; index < currentList.size(); index++) {
            if (isFull()) {
                break;
            }
            if (currentList.get(index).getToFloor() > currentFloor
                    && decisionMaking.getState() == 1) {
                useOutput("IN-" + currentList.get(index).getPersonId()
                        + "-" + currentFloor + "-" + id);
                container.add(currentList.get(index));
                currentList.remove(index);
                index--;
            } else if (currentList.get(index).getToFloor() < currentFloor
                    & decisionMaking.getState() == -1) {
                useOutput("IN-" + currentList.get(index).getPersonId()
                        + "-" + currentFloor + "-" + id);
                container.add(currentList.get(index));
                currentList.remove(index);
                index--;
            }
        }
    }

    // state == 0
    private void initLoad(List<PersonRequest> currentList) {
        if (currentList.size() > 0) {
            useOutput("IN-" + currentList.get(0).getPersonId() + "-" + currentFloor + "-" + id);
            container.add(currentList.get(0));
            currentList.remove(0);
            decisionMaking.initInside();
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
                    useOutput("OUT-" + container.get(index).getPersonId()
                            + "-" + currentFloor + "-" + id);
                    container.remove(index);
                    index--;
                }
            }
        }
    }

    public boolean isFull() {
        return container.size() == max;
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

    public boolean touchable(int floor) {
        return floor >= 1 && floor <= 20;
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

    public int getID() {
        return id;
    }

    public int getDecideMakingState() {
        return decisionMaking.getState();
    }

    public void setInputEnd() {
        inputEnd = true;
    }

    public boolean getInputEnd() {
        return inputEnd;
    }

    public void setMoveSpeed(int ms) {
        moveSpeed = ms;
    }

    public void setMax(int maxin) {
        max = maxin;
    }

    public int getMax() {
        return max;
    }

    public int getInsideNum() {
        return container.size();
    }

    public String getType() {
        return "A";
    }
}
