import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

public class Input {
    public static void main(String[] args) throws Exception {
        // init
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        RequestTable requestTable = new RequestTable();
        ElevatorFactory factory = new ElevatorFactory();
        Output.init();
        // get pattern
        String arrivePattern = elevatorInput.getArrivingPattern();
        //System.out.println(arrivePattern);

        // run
        Thread t = factory.build(requestTable, arrivePattern);
        t.start();

        // get requests
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                requestTable.setFinished();
                break;
            } else {
                // a new valid request
                synchronized (requestTable) {
                    requestTable.receiveRequest(request);
                    //System.out.println(request);
                }
            }
            requestTable.runNotify();
        }

        // check
        //requestTable.checkMap();

        // finish
        elevatorInput.close();
    }
}
