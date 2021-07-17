import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people;
    private final ArrayList<HashMap<Integer, Person>> circles;
    private boolean lasted;

    public MyNetwork() {
        this.people = new HashMap<>();
        this.circles = new ArrayList<>();
        this.lasted = true;
    }

    public boolean contains(int id) {
        return people.containsKey(id);
    }

    public Person getPerson(int id) {
        if (contains(id)) {
            return people.get(id);
        } else {
            return null;
        }
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            /* throw EqualPersonIdException */
            throw new MyEqualPersonIdException(person.getId());
        } else {
            people.put(person.getId(), person);
        }
    }

    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        /* contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2)); */
        if (people.containsKey(id1) && people.containsKey(id2)) {
            MyPerson person1 = (MyPerson) people.get(id1);
            MyPerson person2 = (MyPerson) people.get(id2);
            if (!person1.isLinked(person2)) {
                person1.addAcq(person2, value);
                person2.addAcq(person1, value);
                this.lasted = false;
                return;
            }
        }
        /* improve if */
        if (!people.containsKey(id1)) {
            /* throw PersonIdNotFoundException */
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!people.containsKey(id2)) {
            /* throw PersonIdNotFoundException*/
            throw new MyPersonIdNotFoundException(id2);
        }
        Person person1 = people.get(id1);
        Person person2 = people.get(id2);
        if (person1.isLinked(person2)) {
            /* throw EqualRelationException */
            throw new MyEqualRelationException(id1, id2);
        }
    }

    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        /* contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2)); */
        if (people.containsKey(id1) && people.containsKey(id2)) {
            Person person1 = people.get(id1);
            Person person2 = people.get(id2);
            if (person1.isLinked(person2)) {
                return person1.queryValue(person2);
            }
        }
        /* improve if */
        if (!people.containsKey(id1)) {
            /* throw PersonIdNotFoundException */
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!people.containsKey(id2)) {
            /* throw PersonIdNotFoundException*/
            throw new MyPersonIdNotFoundException(id2);
        }
        Person person1 = people.get(id1);
        Person person2 = people.get(id2);
        if (!person1.isLinked(person2)) {
            /* throw RelationNotFoundException */
            throw new MyRelationNotFoundException(id1, id2);
        }
        return -1;
    }

    public int compareName(int id1, int id2) throws PersonIdNotFoundException {
        /* contains(id1) && contains(id2) */
        if (people.containsKey(id1) && people.containsKey(id2)) {
            Person person1 = people.get(id1);
            Person person2 = people.get(id2);
            return person1.getName().compareTo(person2.getName());
        }
        /* improve if */
        if (!people.containsKey(id1)) {
            /* throw PersonIdNotFoundException */
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!people.containsKey(id2)) {
            /* throw PersonIdNotFoundException */
            throw new MyPersonIdNotFoundException(id2);
        }
        return 0;
    }

    public int queryPeopleSum() {
        return people.size();
    }

    public int queryNameRank(int id) throws PersonIdNotFoundException {
        if (people.containsKey(id)) {
            int rank = 1;
            Person person = people.get(id);
            for (HashMap.Entry<Integer, Person> entry : people.entrySet()) {
                if (entry.getValue().compareTo(person) < 0) {
                    rank++;
                }
            }
            return rank;
        } else  {
            /* throw PersonIdNotFoundException */
            throw new MyPersonIdNotFoundException(id);
        }
    }

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (people.containsKey(id1) && people.containsKey(id2)) {
            /* circle problem here */
            if (id1 == id2) {
                return true;
            }
            Person person1 = people.get(id1);
            Person person2 = people.get(id2);
            HashMap<Integer, Person> circle = inWhichCircle(person1);
            if (circle != null) {
                /* person1 is in the circle */
                if (isPersonInCircle(person2, circle)) {
                    /* person2 is also in the circle */
                    return true;
                }
                if (this.lasted) {
                    /* network not changed, not circle */
                    return false;
                }
                /* person1 in circle & network not lasted */
                return bfs(person1, person2, circle);
            } else {
                /* person1 is not in the circle */
                if (this.lasted) {
                    /* person1 not in any circle & network lasted */
                    return false;
                }
                /* person1 not in circle & network not lasted */
                circle = new HashMap<>();
                boolean isCircle = bfs(person1, person2, circle);
                circles.add(circle);
                return isCircle;
            }
        }

        if (!people.containsKey(id1)) {
            /* throw PersonIdNotFoundException */
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!people.containsKey(id2)) {
            /* throw PersonIdNotFoundException */
            throw new MyPersonIdNotFoundException(id2);
        }
        return false;
    }

    public int queryBlockSum() {
        int blockSum = 0;
        int i = 0;
        int j;
        for (Integer keyI : people.keySet()) {
            j = 0;
            boolean isNewBlock = true;
            for (Integer keyJ : people.keySet()) {
                if (j >= i) {
                    break;
                }
                try {
                    if (isCircle(keyI, keyJ)) {
                        isNewBlock = false;
                        break;
                    }
                } catch (PersonIdNotFoundException e) {
                    /* here need complete */
                }
                j++;
            }
            if (isNewBlock) {
                blockSum++;
            }
            i++;
        }
        return blockSum;
    }

    private boolean bfs(Person start, Person target, HashMap<Integer, Person> circle) {
        HashMap<Integer, Person> mark = new HashMap<>();
        return bfsCore(start, target, circle, mark);
    }

    private boolean bfsCore(Person current, Person target,
                            HashMap<Integer, Person> circle,
                            HashMap<Integer, Person> mark) {
        if (isPersonInCircle(current, mark)) {
            // this point has been visited!
            return false;
        }
        // mark current
        addPersonInCircle(current, mark);

        if (!isPersonInCircle(current, circle)) {
            // current point not in Circle, update Circle
            circle.put(current.getId(), current);
        }

        if (current.equals(target)) {
            // touch target
            return true;
        }

        // not touched target, next layer
        for (Person nextLayer : ((MyPerson) current).getAcquaintance()) {
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
    /*
    private void updateCircles(Person person1, Person person2) {
        HashMap<Integer, Person> circle1 = inWhichCircle(person1);
        HashMap<Integer, Person> circle2 = inWhichCircle(person2);
        if (circle1==null && circle2==null) {
            // point1 & point2 all not in circle
            circle1 = new HashMap<>();
            circle1.put(person1.getId(), person1);
            circle1.put(person2.getId(), person2);
            circles.add(circle1);
        } else if (circle1 == null) {
            // point1 not in circle & point2 in circle
            circle2.put(person1.getId(), person1);
        } else if (circle2 == null) {
            // point2 not in circle & point1 in circle
            circle1.put(person2.getId(), person2);
        } else {
            // point1&2 all in circle
            circle1.putAll(circle2);
            circles.remove(circle2);
        }
    }

    */
}
