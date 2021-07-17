import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import com.oocourse.spec3.main.Network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> id2person;
    private final HashMap<Integer, Group> id2groups;
    private final HashMap<Integer, Message> id2messages;
    private final ArrayList<HashMap<Integer, Person>> circles;
    private final HashMap<Integer, Integer> emojiId2heat;
    private final HashMap<Person, HashMap<Person, Integer>> adjacencyList;
    private int blockSum;

    public MyNetwork() {
        this.id2person = new HashMap<>();
        this.id2messages = new HashMap<>();
        this.id2groups = new HashMap<>();
        this.circles = new ArrayList<>();
        this.emojiId2heat = new HashMap<>();
        this.adjacencyList = new HashMap<>();
        this.blockSum = 0;
    }

    public boolean contains(int id) {
        return id2person.containsKey(id);
    }

    public Person getPerson(int id) {
        return id2person.get(id);
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        if (id2person.containsKey(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            id2person.put(person.getId(), person);
            blockSum++;
        }
    }

    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        MyPerson person1 = (MyPerson) id2person.get(id1);
        MyPerson person2 = (MyPerson) id2person.get(id2);
        if (person1 != null && person2 != null) {
            if (!person1.isLinked(person2)) {
                person1.addAcq(person2, value);
                person2.addAcq(person1, value);
                blockSum = NetworkMethod.updateCircles(blockSum, person1, person2, circles);
                NetworkMethod.upDateGroups(person1, person2, value, id2groups);
                NetworkMethod.upDateAdjacencyList(person1, person2, value, adjacencyList);
                return;
            }
        }
        if (person1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (person2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (person1.isLinked(person2)) {
            throw new MyEqualRelationException(id1, id2);
        }
    }

    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        Person person1 = id2person.get(id1);
        Person person2 = id2person.get(id2);
        if (person1 != null && person2 != null) {
            if (person1.isLinked(person2)) {
                return person1.queryValue(person2);
            }
        }
        if (person1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (person2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (!person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return -1;
    }

    public int compareName(int id1, int id2) throws PersonIdNotFoundException {
        Person person1 = id2person.get(id1);
        Person person2 = id2person.get(id2);
        if (person1 != null && person2 != null) {
            return person1.getName().compareTo(person2.getName());
        }
        if (person1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        throw new MyPersonIdNotFoundException(id2);
    }

    public int queryPeopleSum() {
        return id2person.size();
    }

    public int queryNameRank(int id) throws PersonIdNotFoundException {
        if (id2person.containsKey(id)) {
            int rank = 1;
            Person person = id2person.get(id);
            for (Person loop : id2person.values()) {
                if (loop.compareTo(person) < 0) {
                    rank++;
                }
            }
            return rank;
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (id2person.containsKey(id1) && id2person.containsKey(id2)) {
            if (id1 == id2) {
                return true;
            }
            Person person1 = id2person.get(id1);
            Person person2 = id2person.get(id2);
            HashMap<Integer, Person> circle1 = NetworkMethod.inWhichCircle(person1, circles);
            HashMap<Integer, Person> circle2 = NetworkMethod.inWhichCircle(person2, circles);
            if (circle1 != null && circle2 != null) {
                return circle1 == circle2;
            }
        }
        if (!id2person.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!id2person.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return false;
    }

    public int queryBlockSum() {
        return blockSum;
    }

    public void addGroup(Group group) throws EqualGroupIdException {
        int groupId = group.getId();
        if (!id2groups.containsKey(groupId)) {
            id2groups.put(groupId, group);
        } else {
            throw new MyEqualGroupIdException(groupId);
        }
    }

    public Group getGroup(int id) {
        return id2groups.get(id);
    }

    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        Group group = id2groups.get(id2);
        Person person = id2person.get(id1);
        if (group != null && person != null) {
            if (!group.hasPerson(person) && group.getSize() < 1111) {
                group.addPerson(person);
                return;
            }
        }
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        }
    }

    public int queryGroupSum() {
        return id2groups.size();
    }

    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        Group group = id2groups.get(id);
        if (group != null) {
            return group.getSize();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        Group group = id2groups.get(id);
        if (group != null) {
            return group.getValueSum();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public int queryGroupAgeMean(int id) throws GroupIdNotFoundException {
        Group group = id2groups.get(id);
        if (group != null) {
            return group.getAgeMean();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        Group group = id2groups.get(id);
        if (group != null) {
            return group.getAgeVar();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public boolean containsMessage(int id) {
        return id2messages.containsKey(id);
    }

    public void delFromGroup(int id1, int id2) throws
            GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        Group group = id2groups.get(id2);
        Person person = id2person.get(id1);
        if (group != null && person != null) {
            if (group.hasPerson(person)) {
                group.delPerson(person);
                return;
            }
        }
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        }
    }

    public void addMessage(Message message)
            throws EqualMessageIdException, EqualPersonIdException, EmojiIdNotFoundException {
        int messageId = message.getId();
        boolean existMessage = id2messages.containsKey(messageId);
        boolean existEmoji = false;
        if (message instanceof EmojiMessage) {
            existEmoji = emojiId2heat.containsKey(((EmojiMessage) message).getEmojiId());
        }
        if (existMessage) {
            throw new MyEqualMessageIdException(messageId);
        }
        if (message instanceof EmojiMessage) {
            if (!existEmoji) {
                throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
            }
        }
        int messageType = message.getType();
        boolean personEqual = (message.getPerson1() == message.getPerson2());
        if (messageType == 0 && personEqual) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        id2messages.put(messageId, message);
    }

    public Message getMessage(int id) {
        return id2messages.get(id);
    }

    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        Message message = id2messages.get(id);
        if (message == null) {
            throw new MyMessageIdNotFoundException(id);
        }
        Person person1 = message.getPerson1();
        Person person2 = message.getPerson2();
        Group messageGroup = null;
        boolean hasPerson1 = false;
        int messageType = message.getType();
        boolean isLinked = false;
        boolean personEqual = false;
        if (messageType == 0) {
            isLinked = person1.isLinked(person2);
            personEqual = (person1 == person2);
        } else {
            messageGroup = message.getGroup();
            hasPerson1 = messageGroup.hasPerson(person1);
        }
        if (messageType == 0 && isLinked && !personEqual) {
            sendTypeZeroMessage(message, person1, person2, id);
            return;
        }
        if (messageType == 1 && hasPerson1) {
            sendTypeOneMessage(message, messageGroup, id);
            return;
        }
        if (messageType == 0 && !isLinked) {
            throw new MyRelationNotFoundException(person1.getId(), person2.getId());
        }
        if (messageType == 1 && !hasPerson1) {
            throw new MyPersonIdNotFoundException(person1.getId());
        }
    }

    private void sendTypeZeroMessage(Message message,
                                     Person person1, Person person2, int messageId) {
        int value = message.getSocialValue();
        person1.addSocialValue(value);
        person2.addSocialValue(value);
        id2messages.remove(messageId);
        ((MyPerson) person2).addMessage(message);
        if (message instanceof RedEnvelopeMessage) {
            int money = ((RedEnvelopeMessage) message).getMoney();
            message.getPerson1().addMoney(-money);
            message.getPerson2().addMoney(money);
        }
        if (message instanceof EmojiMessage) {
            int emojiId = ((EmojiMessage) message).getEmojiId();
            if (emojiId2heat.containsKey(emojiId)) {
                int heat = emojiId2heat.get(emojiId);
                emojiId2heat.put(emojiId, heat + 1);
            } else {
                emojiId2heat.put(emojiId, 1);
            }
        }
    }

    private void sendTypeOneMessage(Message message,
                                    Group messageGroup, int messageId) {
        int value = message.getSocialValue();
        for (Person personInGroup : ((MyGroup) messageGroup).getPeople()) {
            personInGroup.addSocialValue(value);
        }
        id2messages.remove(messageId);
        if (message instanceof RedEnvelopeMessage) {
            Group group = message.getGroup();
            Person self = message.getPerson1();
            int money = ((RedEnvelopeMessage) message).getMoney();
            int size = group.getSize();
            int eachEnvelope = money / size;
            self.addMoney(-eachEnvelope * (size - 1));
            List<Person> people = ((MyGroup) group).getPeople();
            for (Person person : people) {
                if (person == self) {
                    continue;
                }
                person.addMoney(eachEnvelope);
            }
        }
        if (message instanceof EmojiMessage) {
            int emojiId = ((EmojiMessage) message).getEmojiId();
            if (emojiId2heat.containsKey(emojiId)) {
                int heat = emojiId2heat.get(emojiId);
                emojiId2heat.put(emojiId, heat + 1);
            } else {
                emojiId2heat.put(emojiId, 1);
            }
        }
    }

    public int querySocialValue(int id) throws PersonIdNotFoundException {
        Person person = id2person.get(id);
        if (person != null) {
            return person.getSocialValue();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        Person person = id2person.get(id);
        if (person != null) {
            return person.getReceivedMessages();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public boolean containsEmojiId(int id) {
        return emojiId2heat.containsKey(id);
    }

    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (emojiId2heat.containsKey(id)) {
            throw new MyEqualEmojiIdException(id);
        }
        emojiId2heat.put(id, 0);
    }

    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (id2person.containsKey(id)) {
            return id2person.get(id).getMoney();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (emojiId2heat.containsKey(id)) {
            return emojiId2heat.get(id);
        } else {
            // throw EmojiIdNotFoundException
            throw new MyEmojiIdNotFoundException(id);
        }
    }

    public int deleteColdEmoji(int limit) {
        Iterator<Integer> iter = emojiId2heat.keySet().iterator();
        int emojiId;
        int heat;
        while (iter.hasNext()) {
            emojiId = iter.next();
            heat = emojiId2heat.get(emojiId);
            if (heat < limit) {
                iter.remove();
                NetworkMethod.removeColdMessage(emojiId, id2messages);
            }
        }
        return emojiId2heat.size();
    }

    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (!id2messages.containsKey(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        Message message = id2messages.get(id);
        if (message.getType() == 1) {
            throw new MyMessageIdNotFoundException(id);
        }
        try {
            if (!isCircle(message.getPerson1().getId(), message.getPerson2().getId())) {
                return -1;
            } else {
                id2messages.remove(id);
                Person person1 = message.getPerson1();
                Person person2 = message.getPerson2();
                int messageValue = message.getSocialValue();
                person1.addSocialValue(messageValue);
                person2.addSocialValue(messageValue);
                if (message instanceof RedEnvelopeMessage) {
                    int money = ((RedEnvelopeMessage) message).getMoney();
                    person1.addMoney(-money);
                    person2.addMoney(money);
                }
                if (message instanceof EmojiMessage) {
                    int emojiId = ((EmojiMessage) message).getEmojiId();
                    if (emojiId2heat.containsKey(emojiId)) {
                        int value = emojiId2heat.get(emojiId);
                        emojiId2heat.put(emojiId, value + 1);
                    } else {
                        emojiId2heat.put(emojiId, 1);
                    }
                }
                ((MyPerson) person2).addMessage(message);
                return NetworkMethod.searchLeastPath(person1, person2, adjacencyList);
            }
        } catch (PersonIdNotFoundException e) {
            return -1;
        }
    }
}