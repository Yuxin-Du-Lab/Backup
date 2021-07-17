import com.oocourse.spec2.exceptions.EqualGroupIdException;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static int globalTimes = 0;
    private static final Counter LOCAL_TIMES = new Counter();
    private final int id;

    public MyEqualGroupIdException(int id) {
        this.id = id;
        globalTimes++;
        LOCAL_TIMES.addIdException(id);
    }

    @Override
    public void print() {
        System.out.println("egi-" + globalTimes + ", " +
                this.id + "-" +
                LOCAL_TIMES.id2ExceptionTimes(this.id)
        );
    }
}
