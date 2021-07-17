import com.oocourse.spec1.exceptions.PersonIdNotFoundException;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static int globalTimes = 0;
    private static final Counter LOCAL_TIMES = new Counter();
    private final int id;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        LOCAL_TIMES.addIdException(this.id);
        globalTimes++;
        //print();
    }

    @Override
    public void print() {
        System.out.println("pinf-" + globalTimes + ", " +
                this.id + "-" + LOCAL_TIMES.id2ExceptionTimes(id));
    }
}
