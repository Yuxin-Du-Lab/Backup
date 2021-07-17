import java.util.ArrayList;
import java.util.List;

public class Bus implements Cloneable{
    private List<Person> personList;

    public Bus() {
        personList = new ArrayList<>();
    }

    public void addPerson(Person person) {
        personList.add(person);
    }

    public void removePerson(Person person) {
        personList.remove(person);
    }

    public List<Person> getPersonList() {
        // TODO: return the current list of persons in the bus by implementing the deep clone
        List<Person> clone = new ArrayList<>();
        for (Person loop: personList) {
            clone.add(loop.clone());
        }
        return clone;
    }
}
