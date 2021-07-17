import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyGroup implements Group {
    private final int id;
    private final HashMap<Person, Integer> person2age;
    private int valueSum;
    private int ageSum;
    private int agePowerSum;

    public MyGroup(int id) {
        this.id = id;
        person2age = new HashMap<>();
        valueSum = 0;
        ageSum = 0;
        agePowerSum = 0;
    }

    public int getId() {
        return this.id;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            return ((Group) obj).getId() == this.id;
        } else {
            return false;
        }
    }

    public void addPerson(Person person) {
        person2age.put(person, person.getAge());
        refreshValueSum(person, true);
        refreshAgeSum(person, true);
    }

    public boolean hasPerson(Person person) {
        return person2age.containsKey(person);
    }

    public int getValueSum() {
        return valueSum;
    }

    public int getAgeMean() {
        if (person2age.size() == 0) {
            return 0;
        } else {
            return ageSum / person2age.size();
        }
    }

    public int getAgeVar() {
        int size = person2age.size();
        if (size == 0) {
            return 0;
        } else {
            int mean = getAgeMean();
            return (agePowerSum + size * mean * mean - 2 * ageSum * mean) / size;
        }
    }

    public void delPerson(Person person) {
        person2age.remove(person);
        refreshValueSum(person, false);
        refreshAgeSum(person, false);
    }

    public int getSize() {
        return person2age.size();
    }

    public List<Person> getPeople() {
        return new ArrayList<>(person2age.keySet());
    }

    private void refreshValueSum(Person person, boolean isAdd) {
        // add person
        for (Person loop : person2age.keySet()) {
            if (loop == person) {
                continue;
            }
            if (person.isLinked(loop)) {
                if (isAdd) {
                    valueSum += 2 * person.queryValue(loop);
                } else {
                    valueSum -= 2 * person.queryValue(loop);
                }
            }
        }
    }

    private void refreshAgeSum(Person person, boolean isAdd) {
        if (isAdd) {
            ageSum += person.getAge();
            agePowerSum += person.getAge() * person.getAge();
        } else {
            ageSum -= person.getAge();
            agePowerSum -= person.getAge() * person.getAge();
        }
    }

    public void addValueSum(int value) {
        valueSum += 2 * value;
    }
}
