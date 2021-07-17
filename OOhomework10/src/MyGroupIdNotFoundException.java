import com.oocourse.spec2.exceptions.GroupIdNotFoundException;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static int globalTimes = 0;
    private static final Counter LOCAL_TIMES = new Counter();
    private final int id;

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        globalTimes++;
        LOCAL_TIMES.addIdException(id);
    }

    @Override
    public void print() {
        System.out.println("ginf-" + globalTimes + ", " +
                this.id + "-" +
                LOCAL_TIMES.id2ExceptionTimes(this.id)
        );
    }
}
