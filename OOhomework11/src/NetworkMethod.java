import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.PriorityQueue;

public class NetworkMethod {

    public static HashMap<Integer, Person> inWhichCircle(
            Person person, ArrayList<HashMap<Integer, Person>> circles) {
        for (HashMap<Integer, Person> circle : circles) {
            if (circle.containsKey(person.getId())) {
                return circle;
            }
        }
        return null;
    }

    public static int updateCircles(int blockSum, Person person1, Person person2,
                                    ArrayList<HashMap<Integer, Person>> circles) {
        HashMap<Integer, Person> circle1 = inWhichCircle(person1, circles);
        HashMap<Integer, Person> circle2 = inWhichCircle(person2, circles);
        int newBlockSum = blockSum;
        if (circle1 == null && circle2 == null) {
            circle1 = new HashMap<>();
            circle1.put(person1.getId(), person1);
            circle1.put(person2.getId(), person2);
            circles.add(circle1);
            newBlockSum--;
        } else if (circle1 == null) {
            circle2.put(person1.getId(), person1);
            newBlockSum--;
        } else if (circle2 == null) {
            circle1.put(person2.getId(), person2);
            newBlockSum--;
        } else {
            if (circle1 != circle2) {
                circle1.putAll(circle2);
                circles.remove(circle2);
                newBlockSum--;
            }
        }
        return newBlockSum;
    }

    public static void upDateGroups(Person person1, Person person2, int value,
                                    HashMap<Integer, Group> id2groups) {
        for (Group group : id2groups.values()) {
            if (group.hasPerson(person1) && group.hasPerson(person2)) {
                ((MyGroup) group).addValueSum(value);
            }
        }
    }

    public static void removeColdMessage(int id,
                                         HashMap<Integer, Message> id2messages) {
        Iterator<Message> iter = id2messages.values().iterator();
        Message o;
        while (iter.hasNext()) {
            o = iter.next();
            if (o instanceof EmojiMessage) {
                if (((EmojiMessage) o).getEmojiId() == id) {
                    iter.remove();
                }
            }
        }
    }

    public static int searchLeastPath(
            Person source, Person target,
            HashMap<Person, HashMap<Person, Integer>> adjacencyList) {
        // result
        HashMap<Person, Integer> person2distance = new HashMap<>();
        person2distance.put(source, 0);

        // not be added edges
        Queue<Couple> queue = new PriorityQueue<>();
        Person freshman = source;
        int baseDistance = 0;
        while (!person2distance.containsKey(target)) {
            for (Person person : adjacencyList.get(freshman).keySet()) {
                if (person2distance.containsKey(person)) {
                    continue;
                }
                Couple couple = new Couple(source, person,
                        adjacencyList.get(freshman).get(person) + baseDistance);
                queue.add(couple);
            }
            Couple couple = queue.poll();
            if (couple == null) {
                continue;
            }
            freshman = couple.getPerson2();
            baseDistance = couple.getDistance();
            person2distance.put(freshman, baseDistance);
        }
        return person2distance.get(target);
    }

    public static void upDateAdjacencyList(
            Person person1, Person person2, int value,
            HashMap<Person, HashMap<Person, Integer>> adjacencyList) {
        if (adjacencyList.containsKey(person1)) {
            adjacencyList.get(person1).put(person2, value);
        } else {
            HashMap<Person, Integer> person2value = new HashMap<>();
            person2value.put(person2, value);
            adjacencyList.put(person1, person2value);
        }

        if (adjacencyList.containsKey(person2)) {
            adjacencyList.get(person2).put(person1, value);
        } else {
            HashMap<Person, Integer> person2value = new HashMap<>();
            person2value.put(person1, value);
            adjacencyList.put(person2, person2value);
        }
    }

}