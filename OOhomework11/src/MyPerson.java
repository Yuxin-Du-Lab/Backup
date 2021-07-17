import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private int socialValue;
    private int money;

    private final HashMap<Person, Integer> person2value = new HashMap<>();
    private final List<Message> messages = new ArrayList<>();

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.socialValue = 0;
        this.money = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            return (((Person) obj).getId() == id);
        } else {
            return false;
        }
    }

    public boolean isLinked(Person person) {
        if (person.getId() == this.id) {
            return true;
        }
        return person2value.containsKey(person);
    }

    public int queryValue(Person person) {
        boolean find = person2value.containsKey(person);
        if (find) {
            return person2value.get(person);
        } else {
            return 0;
        }
    }

    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    public void addSocialValue(int num) {
        socialValue += num;
    }

    public void addMessage(Message message) {
        messages.add(0, message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public int getSocialValue() {
        return socialValue;
    }

    public List<Message> getReceivedMessages() {
        List<Message> receive = new ArrayList<>();
        for (int i = 0; i < 4 && i < messages.size(); i++) {
            receive.add(messages.get(i));
        }
        return receive;
    }

    public void addMoney(int num) {
        //System.out.println(id + "(" + money + ")" + " get money " + num);
        money += num;
    }

    public int getMoney() {
        return money;
    }

    public void addAcq(Person person, int value) {
        person2value.put(person, value);
    }

    public ArrayList<Person> getAcquaintance() {
        return new ArrayList<>(person2value.keySet());
    }
}
