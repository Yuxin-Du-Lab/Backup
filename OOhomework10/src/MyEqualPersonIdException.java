import com.oocourse.spec2.exceptions.EqualPersonIdException;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static int globalTimes = 0;
    private static final Counter LOCAL_TIMES = new Counter();
    private final int id;

    public MyEqualPersonIdException(int id) {
        this.id = id;
        LOCAL_TIMES.addIdException(this.id);
        globalTimes++;
        //print();
    }

    @Override
    public void print() {
        System.out.println("epi-" + globalTimes + ", " +
                this.id + "-" + LOCAL_TIMES.id2ExceptionTimes(this.id));
    }
}
