import com.oocourse.spec3.main.Person;

public class Couple implements Comparable<Couple> {
    private final Person person1;
    private final Person person2;
    private final int distance;

    public Couple(Person p1, Person p2, int currentDistance) {
        person1 = p1;
        person2 = p2;
        distance = currentDistance;
    }

    public int getDistance() {
        return distance;
    }

    public boolean equal(Person p1, Person p2) {
        return person1 == p1 && person2 == p2 || person1 == p2 && person2 == p1;
    }

    public Person getPerson2() {
        return person2;
    }

    public int compareTo(Couple couple) {
        return this.distance - couple.getDistance();
    }
}
