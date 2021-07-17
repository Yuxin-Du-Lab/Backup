import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalTable {
    private final HashMap<Integer, List<PersonRequest>> localMap = new HashMap<>();

    public LocalTable() {
        for (int floor = 1; floor <= 20; floor++) {
            List<PersonRequest> list = new ArrayList<>();
            localMap.put(floor, list);
        }
    }

    public synchronized void receiveRequest(PersonRequest request) {
        //System.out.println("Local Table receive " + request);
        localMap.get(request.getFromFloor()).add(request);
        notifyAll();
    }

    public synchronized List<PersonRequest> getFloorList(int floor) {
        return localMap.get(floor);
    }

    public synchronized void runWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            // do nothing
        }
    }

    public void checkMap() {
        System.out.println(localMap);
    }

    public synchronized int getWaitingNum() {
        int num = 0;
        for (int floor = 1; floor <= 20; floor++) {
            num += localMap.get(floor).size();
        }
        return num;
    }
}
