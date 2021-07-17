import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.Request;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.ElevatorRequest;

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

        // init elevator 1~3
        for (int i = 1; i <= 3; i++) {
            Thread t;
            if (i == 1) {
                t = elevatorFactory.build(arrivingPattern, i, "A");
            } else if (i == 2) {
                t = elevatorFactory.build(arrivingPattern, i, "B");
            } else {
                t = elevatorFactory.build(arrivingPattern, i, "C");
            }
            t.start();
            synchronized (elevatorList) {
                elevatorList.addElevator((Elevator) t);
            }
        }

        // run dispatch
        Thread dispatch = new Dispatch(waitQueue, elevatorList, arrivingPattern);
        dispatch.start();



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
                    String type = ((ElevatorRequest) request).getElevatorType();
                    Thread elevator = elevatorFactory.build(arrivingPattern, id, type);
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
