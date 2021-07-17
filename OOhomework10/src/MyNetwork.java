import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> id2person;
    private final HashMap<Integer, Group> groups;
    private final HashMap<Integer, Message> messages;
    private final ArrayList<HashMap<Integer, Person>> circles;
    private int blockSum;

    public MyNetwork() {
        this.id2person = new HashMap<>();
        this.messages = new HashMap<>();
        this.groups = new HashMap<>();
        this.circles = new ArrayList<>();
        blockSum = 0;
    }

    public boolean contains(int id) {
        return id2person.containsKey(id);
    }

    public Person getPerson(int id) {
        return id2person.get(id);
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        if (id2person.containsKey(person.getId())) {
            /* throw EqualPersonIdException */
            throw new MyEqualPersonIdException(person.getId());
        } else {
            id2person.put(person.getId(), person);
            blockSum++;
        }
    }

    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        /* id1 != id2 */
        /* contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2)); */
        MyPerson person1 = (MyPerson) id2person.get(id1);
        MyPerson person2 = (MyPerson) id2person.get(id2);
        if (person1 != null && person2 != null) {
            if (!person1.isLinked(person2)) {
                person1.addAcq(person2, value);
                person2.addAcq(person1, value);
                updateCircles(person1, person2);
                upDateGroups(person1, person2, value);
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
        /* contains(id1) && contains(id2) */
        Person person1 = id2person.get(id1);
        Person person2 = id2person.get(id2);
        if (person1 != null && person2 != null) {
            return person1.getName().compareTo(person2.getName());
        }
        /* improve if */
        if (person1 == null) {
            /* throw PersonIdNotFoundException */
            throw new MyPersonIdNotFoundException(id1);
        }
        if (person2 == null) {
            /* throw PersonIdNotFoundException */
            throw new MyPersonIdNotFoundException(id2);
        }
        return 0;
    }

    public int queryPeopleSum() {
        return id2person.size();
    }

    public int queryNameRank(int id) throws PersonIdNotFoundException {
        /* here can add cache */
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
            HashMap<Integer, Person> circle1 = inWhichCircle(person1);
            HashMap<Integer, Person> circle2 = inWhichCircle(person2);
            if (circle1 != null && circle2 != null) {
                return circle1 == circle2;
            }
        }

        if (!id2person.containsKey(id1)) {
            /* throw PersonIdNotFoundException */
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!id2person.containsKey(id2)) {
            /* throw PersonIdNotFoundException */
            throw new MyPersonIdNotFoundException(id2);
        }
        return false;
    }

    public int queryBlockSum() {
        return blockSum;
    }

    public void addGroup(Group group) throws EqualGroupIdException {
        /* not exist group */
        int groupId = group.getId();
        if (!groups.containsKey(groupId)) {
            groups.put(groupId, group);
        } else {
            /* EqualGroupIdException e */
            throw new MyEqualGroupIdException(groupId);
        }
    }

    public Group getGroup(int id) {
        return groups.get(id);
    }

    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        /* groups exist id2 && people exist id1 */
        /* group id2 do not have person id1*/
        /* group id2 length < 1111 */
        Group group = groups.get(id2);
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
        return groups.size();
    }

    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        Group group = groups.get(id);
        if (group != null) {
            return group.getSize();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        Group group = groups.get(id);
        if (group != null) {
            return group.getValueSum();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public int queryGroupAgeMean(int id) throws GroupIdNotFoundException {
        Group group = groups.get(id);
        if (group != null) {
            return group.getAgeMean();
        } else {
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        Group group = groups.get(id);
        if (group != null) {
            return group.getAgeVar();
        } else {
            /* throw GroupIdNotFoundException e */
            throw new MyGroupIdNotFoundException(id);
        }
    }

    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    public void delFromGroup(int id1, int id2) throws
            GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        Group group = groups.get(id2);
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

    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        int messageId = message.getId();
        boolean existMessage = messages.containsKey(messageId);
        int messageType = message.getType();
        boolean personEqual = (message.getPerson1() == message.getPerson2());
        if (!existMessage && ((messageType == 0 && !personEqual) || messageType == 1)) {
            messages.put(messageId, message);
            return;
        }
        if (existMessage) {
            throw new MyEqualMessageIdException(messageId);
        }
        if (messageType == 0 && personEqual) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
    }

    public Message getMessage(int id) {
        return messages.get(id);
    }

    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        Message message = messages.get(id);
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
            int value = message.getSocialValue();
            person1.addSocialValue(value);
            person2.addSocialValue(value);
            messages.remove(id);
            ((MyPerson) person2).addMessage(message);
            return;
        }

        if (messageType == 1 && hasPerson1) {
            int value = message.getSocialValue();
            for (Person personInGroup : ((MyGroup) messageGroup).getPeople()) {
                personInGroup.addSocialValue(value);
            }
            messages.remove(id);
            return;
        }

        if (messageType == 0 && !isLinked) {
            /* throw RelationNotFoundException e */
            throw new MyRelationNotFoundException(person1.getId(), person2.getId());
        }

        if (messageType == 1 && !hasPerson1) {
            /* throw PersonIdNotFoundException e */
            throw new MyPersonIdNotFoundException(person1.getId());
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

    private boolean bfs(Person start, Person target, HashMap<Integer, Person> circle) {
        HashMap<Integer, Person> mark = new HashMap<>();
        return bfsCore(start, target, circle, mark);
    }

    private boolean bfsCore(Person current, Person target,
                            HashMap<Integer, Person> circle,
                            HashMap<Integer, Person> mark) {
        if (isPersonInCircle(current, mark)) {
            return false;
        }
        addPersonInCircle(current, mark);
        if (!isPersonInCircle(current, circle)) {
            circle.put(current.getId(), current);
        }
        if (current.equals(target)) {
            return true;
        }
        ArrayList<Person> people = ((MyPerson) current).getAcquaintance();
        for (Person nextLayer : people) {
            if (bfsCore(nextLayer, target, circle, mark)) {
                return true;
            }
        }
        return false;
    }

    private void addPersonInCircle(Person person, HashMap<Integer, Person> circle) {
        circle.put(person.getId(), person);
    }

    private boolean isPersonInCircle(Person person, HashMap<Integer, Person> circle) {
        return circle.containsKey(person.getId());
    }

    private HashMap<Integer, Person> inWhichCircle(Person person) {
        for (HashMap<Integer, Person> circle : circles) {
            if (isPersonInCircle(person, circle)) {
                return circle;
            }
        }
        return null;
    }

    private void updateCircles(Person person1, Person person2) {
        HashMap<Integer, Person> circle1 = inWhichCircle(person1);
        HashMap<Integer, Person> circle2 = inWhichCircle(person2);
        if (circle1 == null && circle2 == null) {
            circle1 = new HashMap<>();
            circle1.put(person1.getId(), person1);
            circle1.put(person2.getId(), person2);
            circles.add(circle1);
            blockSum--;
        } else if (circle1 == null) {
            circle2.put(person1.getId(), person1);
            blockSum--;
        } else if (circle2 == null) {
            circle1.put(person2.getId(), person2);
            blockSum--;
        } else {
            if (circle1 != circle2) {
                circle1.putAll(circle2);
                circles.remove(circle2);
                blockSum--;
            }
        }
    }

    private void upDateGroups(Person person1, Person person2, int value) {
        for (Group group: groups.values()) {
            if (group.hasPerson(person1) && group.hasPerson(person2)) {
                ((MyGroup) group).addValueSum(value);
            }
        }
    }
}
