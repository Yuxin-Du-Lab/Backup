import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final ArrayList<Person> acquaintance = new ArrayList<>();
    private final ArrayList<Integer> value = new ArrayList<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            return (((Person) obj).getId() == this.id);
        } else {
            return false;
        }
    }

    public boolean isLinked(Person person) {
        if (person.getId() == this.id) {
            return true;
        }
        return checkAcq(person) >= 0;
    }

    public int queryValue(Person person) {
        int find = checkAcq(person);
        if (find >= 0) {
            return value.get(find);
        } else {
            return 0;
        }
    }

    public int compareTo(Person p2) {
        return this.name.compareTo(p2.getName());
    }

    public void addAcq(Person person, int value) {
        this.acquaintance.add(person);
        this.value.add(value);
    }

    public ArrayList<Person> getAcquaintance() {
        return this.acquaintance;
    }

    private int checkAcq(Person person) {
        /* Time complexity here can be improved */
        int cnt = 0;
        int personId = person.getId();
        for (Person loop : this.acquaintance) {
            if (loop.getId() == personId) {
                return cnt;
            }
            cnt++;
        }
        return -1;
    }
}
