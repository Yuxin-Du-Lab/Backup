import com.oocourse.spec2.exceptions.RelationNotFoundException;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static int globalTimes = 0;
    private static final Counter LOCAL_TIMES = new Counter();
    private final int id1;
    private final int id2;

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        LOCAL_TIMES.addIdException(this.id1);
        LOCAL_TIMES.addIdException(this.id2);
        globalTimes++;
        //print();
    }

    @Override
    public void print() {
        if (this.id1 <= this.id2) {
            System.out.println("rnf-" + globalTimes + ", " +
                    this.id1 + "-" + LOCAL_TIMES.id2ExceptionTimes(id1) + ", " +
                    this.id2 + "-" + LOCAL_TIMES.id2ExceptionTimes(id2));
        } else {
            System.out.println("rnf-" + globalTimes + ", " +
                    this.id2 + "-" + LOCAL_TIMES.id2ExceptionTimes(id2) + ", " +
                    this.id1 + "-" + LOCAL_TIMES.id2ExceptionTimes(id1));
        }
    }
}
