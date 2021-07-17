import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

public class MyNoticeMessage extends MyMessage implements NoticeMessage {
    /*
    private final int id;
    private final int socialValue;
    private final int type;
    private final Person person1;
    private final Person person2;
    private final Group group;
     */
    private final String notice;

    public MyNoticeMessage(int messageId, String noticeString,
                           Person messagePerson1, Person messagePerson2) {
        super(messageId, noticeString.length(), messagePerson1, messagePerson2);
        this.notice = noticeString;
    }

    public MyNoticeMessage(int messageId, String noticeString,
                           Person messagePerson1, Group messageGroup) {
        super(messageId, noticeString.length(), messagePerson1, messageGroup);
        this.notice = noticeString;
    }

    public String getString() {
        return notice;
    }
}
