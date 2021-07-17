import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

public class MyRedEnvelopeMessage extends MyMessage implements RedEnvelopeMessage {
    /*
    private final int id;
    private final int socialValue;
    private final int type;
    private final Person person1;
    private final Person person2;
    private final Group group;
     */
    private final int luckyMoney;

    public MyRedEnvelopeMessage(int messageId, int luckyMoney,
                                Person messagePerson1, Person messagePerson2) {
        super(messageId, luckyMoney * 5, messagePerson1, messagePerson2);
        this.luckyMoney = luckyMoney;
    }

    public MyRedEnvelopeMessage(int messageId, int luckyMoney,
                                Person messagePerson1, Group messageGroup) {
        super(messageId, luckyMoney * 5, messagePerson1, messageGroup);
        this.luckyMoney = luckyMoney;
    }

    public int getMoney() {
        return luckyMoney;
    }
}
