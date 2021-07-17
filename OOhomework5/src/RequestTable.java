import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestTable {
    private final HashMap<Integer, List<PersonRequest>> queueMap = new HashMap<>();
    private boolean isFinished;

    public RequestTable() {
        isFinished = false;
        for (int floor = 1; floor <= 20; floor++) {
            List<PersonRequest> list = new ArrayList<>();
            queueMap.put(floor, list);
        }
    }

    public void receiveRequest(PersonRequest request) {
        queueMap.get(request.getFromFloor()).add(request);
    }

    public List<PersonRequest> getFloorList(int floor) {
        return queueMap.get(floor);
    }

    public synchronized void runWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            // do nothing
        }
    }

    public synchronized void runNotify() {
        notifyAll();
    }

    public void checkMap() {
        System.out.println(queueMap);
    }

    public int getWaitingNum() {
        int num = 0;
        for (int floor = 1; floor <= 20; floor++) {
            num += queueMap.get(floor).size();
        }
        return num;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public void setFinished() {
        isFinished = true;
    }
}
