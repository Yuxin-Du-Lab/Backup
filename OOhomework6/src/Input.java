import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.Request;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.ElevatorRequest;

public class Input {
    public static void main(String[] args) throws Exception {
        // init
        Output.init();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        ElevatorFactory elevatorFactory = new ElevatorFactory();
        WaitQueue waitQueue = new WaitQueue();
        ElevatorList elevatorList = new ElevatorList();

        // get pattern
        String arrivingPattern = elevatorInput.getArrivingPattern();
        //System.out.println(arrivingPattern);

        // run dispatch
        Thread dispatch = new Dispatch(waitQueue, elevatorList, arrivingPattern);
        dispatch.start();

        // init elevator 1~3
        for (int i = 1; i <= 3; i++) {
            Thread t = elevatorFactory.build(arrivingPattern, i);
            t.start();
            synchronized (elevatorList) {
                elevatorList.addElevator((Elevator) t);
            }
        }

        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                //set waitQueue finished
                synchronized (waitQueue) {
                    waitQueue.setFinished();
                    waitQueue.notifyAll();
                }
                //System.out.println("Input die!");
                break;
            } else {
                // a new valid request
                if (request instanceof PersonRequest) {
                    // a PersonRequest
                    // your code here
                    synchronized (waitQueue) {
                        waitQueue.pushRequest((PersonRequest) request);
                        waitQueue.notifyAll();
                    }
                    //System.out.println("A PersonRequest:    " + request);
                } else if (request instanceof ElevatorRequest) {
                    // an ElevatorRequest
                    // your code here
                    int id = Integer.parseInt(((ElevatorRequest) request).getElevatorId());
                    Thread elevator = elevatorFactory.build(arrivingPattern, id);
                    elevator.start();
                    synchronized (elevatorList) {
                        elevatorList.addElevator((Elevator) elevator);
                        //System.out.println("add elevator");
                    }
                    //System.out.println("An ElevatorRequest: " + request);
                }
            }
        }
        elevatorInput.close();
    }
}
