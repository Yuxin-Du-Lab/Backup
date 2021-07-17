import com.oocourse.spec2.exceptions.EqualMessageIdException;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static int globalTimes = 0;
    private static final Counter LOCAL_TIMES = new Counter();
    private final int id;

    public MyEqualMessageIdException(int id) {
        this.id = id;
        globalTimes++;
        LOCAL_TIMES.addIdException(id);
    }

    @Override
    public void print() {
        System.out.println("emi-" + globalTimes + ", " +
                this.id + "-" +
                LOCAL_TIMES.id2ExceptionTimes(this.id)
        );
    }
}
