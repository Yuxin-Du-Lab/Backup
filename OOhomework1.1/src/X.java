public class X implements Base {
    @Override
    public String toString() {
        return "x";
    }

    @Override
    public int diff() {
        return 1;
    }

    @Override
    public boolean equal(Base newBase) {
        if (newBase.toString().equals(toString())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String check() {
        return "x";
    }
}
