import java.util.List;

public class Factory {
    public static Vehicle getNew(List<String> ops) {
        String type = ops.get(1);
        if ("Car".equals(type)) {
            // TODO
            return new Car(Integer.parseInt(ops.get(2)),Integer.parseInt(ops.get(3)),Integer.parseInt(ops.get(4)));
        } else if ("Sprinkler".equals(type)) {
            // TODO
            return new Sprinkler(Integer.parseInt(ops.get(2)),Integer.parseInt(ops.get(3)),Integer.parseInt(ops.get(4)));
        } else {
            // TODO
            return new Bus(Integer.parseInt(ops.get(2)),Integer.parseInt(ops.get(3)),Integer.parseInt(ops.get(4)));
        }
    }
}
