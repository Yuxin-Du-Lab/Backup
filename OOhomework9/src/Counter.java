import java.util.HashMap;

public class Counter {
    private HashMap<Integer, Integer> idToTimes = new HashMap<>();

    public void addIdException(int id) {
        if (idToTimes.containsKey(id)) {
            idToTimes.replace(id, idToTimes.get(id) + 1);
        } else {
            idToTimes.put(id, 1);
        }
    }

    public int id2ExceptionTimes(int id) {
        return idToTimes.get(id);
    }
}
