public class Constant {
    public int value;

    public Constant(String constantString) {
        value = Integer.parseInt(constantString);
    }

    public int diff() {
        return 0;
    }

    public void update(Constant newConstant) {
        value *= newConstant.value;
    }

    public String check() {
        return Integer.toString(value);
    }

    public void combine(Constant newConstant) {
        value += newConstant.value;
    }
}
