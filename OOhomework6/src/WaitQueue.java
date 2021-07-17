import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.List;

public class WaitQueue {
    private List<PersonRequest> waitQueue;
    private boolean isFinished;

    public WaitQueue() {
        isFinished = false;
        waitQueue = new ArrayList<>();
    }

    public synchronized void pushRequest(PersonRequest requestIn) {
        //System.out.println("push request in wait queue");
        waitQueue.add(requestIn);
    }

    public synchronized PersonRequest popRequest() {
        PersonRequest head = waitQueue.get(0);
        waitQueue.remove(0);
        //System.out.println("pop Request:" + head);
        return head;
    }

    public void setFinished() {
        isFinished = true;
    }

    public boolean getFinished() {
        return isFinished;
    }

    public synchronized int getLenth() {
        return waitQueue.size();
    }

    public synchronized void runWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            // do nothing
        }
    }
}
