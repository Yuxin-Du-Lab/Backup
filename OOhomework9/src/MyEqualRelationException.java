import com.oocourse.spec1.exceptions.EqualRelationException;

public class MyEqualRelationException extends EqualRelationException {
    private static int globalTimes = 0;
    private static final Counter LOCAL_TIMES = new Counter();
    private final int id1;
    private final int id2;

    public MyEqualRelationException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        globalTimes++;
        if (id1 == id2) {
            LOCAL_TIMES.addIdException(id1);
        } else {
            LOCAL_TIMES.addIdException(id1);
            LOCAL_TIMES.addIdException(id2);
        }
        //print();
    }

    @Override
    public void print() {
        if (this.id1 <= this.id2) {
            System.out.println("er-" + globalTimes + ", " +
                    this.id1 + "-" + LOCAL_TIMES.id2ExceptionTimes(this.id1) + ", " +
                    this.id2 + "-" + LOCAL_TIMES.id2ExceptionTimes(this.id2));
        } else {
            System.out.println("er-" + globalTimes + ", " +
                    this.id2 + "-" + LOCAL_TIMES.id2ExceptionTimes(this.id2) + ", " +
                    this.id1 + "-" + LOCAL_TIMES.id2ExceptionTimes(this.id1));
        }
    }
}
