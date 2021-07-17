import com.oocourse.elevator2.PersonRequest;

public class DecisionMaking {
    //private for checkStyle....
    private PersonRequest mainRequest = null;
    private int state;  //0, wait; 1, up; -1, down
    private Elevator elevator;
    private LocalTable localTable;

    public DecisionMaking() {
        elevator = null;
        localTable = null;
        state = 0;
    }

    public void init(Elevator elevatorIn) {
        elevator = elevatorIn;
        localTable = elevatorIn.getLocalTable();
    }

    public String decide() {
        String order = "WAIT";
        if (mainRequest == null) {
            // check inside
            if (elevator.getContainer().size() > 0) {
                initInside();
            } else if (state == 0) {
                // look up and down
                state = lookUpAndDown(elevator.getCurrentFloor());
            } else if (state == 1) {
                // going up, look up
                state = loopUp(elevator.getCurrentFloor());
            } else if (state == -1) {
                // going down, look down
                state = loopDown(elevator.getCurrentFloor());
            }
            order = goByState();
        } else {
            order = go2MainRequest();
        }
        // check current floor's LS
        if ((!elevator.isFull()) && checkFloor4LS(elevator.getCurrentFloor())) {
            // elevator not full & LS exist this floor
            order = "OPEN";
        }
        // someone inside wants out
        if (isSomeoneArrive(elevator.getCurrentFloor())) {
            order = "OPEN";
        }
        return order;
    }

    public void initInside() {
        if (elevator.getContainer().size() > 0) {
            this.mainRequest = elevator.getContainer().get(0);
            this.state = (this.mainRequest.getToFloor() > elevator.getCurrentFloor()) ? 1 : -1;
        } else {
            System.out.println("Init failed!");
        }
    }

    protected Boolean isSomeoneArrive(int floor) {
        for (PersonRequest man : elevator.getContainer()) {
            if (man.getToFloor() == floor) {
                return true;
            }
        }
        return false;
    }

    protected int lookUpAndDown(int floor) {
        for (int downBoundary = 1; downBoundary <= 20; downBoundary++) {
            int upBoundary = 20 - downBoundary + 1;
            if (localTable.getFloorList(downBoundary).size() > 0 && downBoundary < floor) {
                return -1;
            }
            if (localTable.getFloorList(upBoundary).size() > 0 && upBoundary > floor) {
                return 1;
            }
        }
        return 0;
    }

    protected int loopUp(int floor) {
        for (int upBoundary = 20; upBoundary > floor; upBoundary--) {
            if (localTable.getFloorList(upBoundary).size() > 0) {
                return 1;
            }
        }
        return 0;
    }

    protected int loopDown(int floor) {
        for (int downBoundary = 1; downBoundary < floor; downBoundary++) {
            if (localTable.getFloorList(downBoundary).size() > 0) {
                return -1;
            }
        }
        return 0;
    }

    protected String goByState() {
        if (state == 0) {
            return "WAIT";
        } else if (state == 1) {
            return "UP";
        } else if (state == -1) {
            return "DOWN";
        }
        return "ERROR STATE";
    }

    protected String go2MainRequest() {
        int current = elevator.getCurrentFloor();
        int goTo = mainRequest.getToFloor();
        if (current == goTo) {
            // Arrive
            state = 0;
            mainRequest = null;
            return "OPEN";
        } else if (current < goTo) {
            state = 1;
            return "UP";
        } else {
            state = -1;
            return "DOWN";
        }
    }

    protected boolean checkFloor4LS(int floor) {
        for (PersonRequest man : elevator.getLocalTable().getFloorList(floor)) {
            if ((man.getToFloor() > floor) & (state == 1)) {
                return true;
            } else if ((man.getToFloor() < floor) & (state == -1)) {
                return true;
            } else if (state == 0) {
                return true;
            }
        }
        return false;
    }

    public int getState() {
        return state;
    }

    protected Elevator getElevator() {
        return elevator;
    }

    protected void setState(int state1) {
        state = state1;
    }

    protected LocalTable getRequestTable() {
        return localTable;
    }

    protected PersonRequest getMainRequest() {
        return mainRequest;
    }
}
