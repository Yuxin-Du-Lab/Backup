import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

public class MyEmojiMessage extends MyMessage implements EmojiMessage {
    /*
    private final int id;
    private final int socialValue;
    private final int type;
    private final Person person1;
    private final Person person2;
    private final Group group;
     */
    private final int id;

    public MyEmojiMessage(int messageId, int emojiNumber,
                          Person messagePerson1, Person messagePerson2) {
        super(messageId, emojiNumber, messagePerson1, messagePerson2);
        this.id = emojiNumber;
    }

    public MyEmojiMessage(int messageId, int emojiNumber,
                          Person messagePerson1, Group messageGroup) {
        super(messageId, emojiNumber, messagePerson1, messageGroup);
        this.id = emojiNumber;
    }

    public int getEmojiId() {
        return id;
    }
}
