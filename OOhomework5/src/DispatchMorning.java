public class DispatchMorning extends Dispatch {
    public DispatchMorning() {
        super();
    }

    @Override
    public String decide() {
        String order = "WAIT";
        if (super.getMainRequest() == null) {
            // check inside
            if (super.getElevator().getContainer().size() > 0) {
                initInside();
            } else if (super.getState() == 0) {
                // look up and down
                super.setState(lookUpAndDown(super.getElevator().getCurrentFloor()));
            } else if (super.getState() == 1) {
                // going up, look up
                super.setState(loopUp(super.getElevator().getCurrentFloor()));
            } else if (super.getState() == -1) {
                // going down, look down
                super.setState(loopDown(super.getElevator().getCurrentFloor()));
            }
            order = goByState();
        } else {
            order = go2MainRequest();
        }
        // check current floor's LS
        if ((!super.getElevator().isFull())
                && checkFloor4LS(super.getElevator().getCurrentFloor())) {
            // elevator not full & LS exist this floor
            order = "OPEN";
        }
        // someone inside wants out
        if (isSomeoneArrive(super.getElevator().getCurrentFloor())) {
            order = "OPEN";
        }
        // how to code it?
        if (super.getElevator().getCurrentFloor() == 1
                & !super.getElevator().isFull()
                & (!super.getElevator().getRequestTable().getIsFinished()
                | super.getRequestTable().getFloorList(1).size() > 0)) {
            // F1 & not full & not finished -> OPENING
            order = "OPENING";
        } else if (super.getElevator().getCurrentFloor() == 1
                & (super.getElevator().isFull()
                | super.getElevator().getRequestTable().getIsFinished())
                & super.getElevator().isDoorOpened()) {
            order = "CLOSE";
        }
        return order;
    }

    public void standby() {
        // wait for next man
        try {
            super.getElevator().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
