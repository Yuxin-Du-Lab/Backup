import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private static int globalTimes = 0;
    private static final Counter LOCAL_TIMES = new Counter();
    private final int id;

    public MyEmojiIdNotFoundException(int id) {
        this.id = id;
        globalTimes++;
        LOCAL_TIMES.addIdException(id);
    }

    @Override
    public void print() {
        System.out.println("einf-" + globalTimes + ", " +
                this.id + "-" +
                LOCAL_TIMES.id2ExceptionTimes(this.id)
        );
    }
}
